package teamdevhub.devhub.auth.outbound.auth.infrastructure.oauth.github;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;
import teamdevhub.devhub.auth.core.auth.port.out.oauth.OAuthClient;
import teamdevhub.devhub.auth.outbound.auth.infrastructure.oauth.OAuthHttpClient;
import teamdevhub.devhub.auth.core.auth.domain.vo.oauth.OAuthUser;
import teamdevhub.devhub.auth.outbound.auth.infrastructure.oauth.github.config.GithubOAuthConfig;
import teamdevhub.devhub.auth.outbound.auth.infrastructure.oauth.github.vo.GithubEmailResponse;
import teamdevhub.devhub.auth.outbound.auth.infrastructure.oauth.github.vo.GithubTokenResponse;
import teamdevhub.devhub.auth.outbound.auth.infrastructure.oauth.github.vo.GithubUserResponse;
import teamdevhub.devhub.auth.outbound.auth.infrastructure.oauth.http.BearerAuthHeaderProvider;
import teamdevhub.devhub.auth.outbound.auth.infrastructure.oauth.http.HttpResponse;
import teamdevhub.devhub.auth.core.auth.domain.VerificationProvider;

import java.net.URI;
import java.util.Arrays;

@Slf4j
@Component
@RequiredArgsConstructor
public class GithubOAuthClientAdapter implements OAuthClient {

    private final OAuthHttpClient oauthHttpClient;
    private final GithubOAuthConfig githubOAuthConfig;

    @Override
    public boolean supports(VerificationProvider verificationProvider) {
        return verificationProvider == VerificationProvider.GITHUB;
    }

    @Override
    public String getAuthorizationUrl(String state) {
        return UriComponentsBuilder
                .fromUri(URI.create(githubOAuthConfig.getAuthorizationUri()))
                .queryParam("client_id", githubOAuthConfig.getClientId())
                .queryParam("redirect_uri", githubOAuthConfig.getRedirectUri())
                .queryParam("scope", "read:user user:email")
                .queryParam("allow_signup", "true")
                .queryParam("state", state)
                .build()
                .toUriString();
    }

    @Override
    public OAuthUser fetchUser(String code) {
        String accessToken = getAccessToken(code);

        HttpResponse<GithubUserResponse> response = oauthHttpClient.get(githubOAuthConfig.getUserInfoUri(), new BearerAuthHeaderProvider(accessToken), GithubUserResponse.class);

        if (!response.is2xx() || response.body() == null) {
            throw new RuntimeException("Github 사용자 조회 실패: " + response.rawBody());
        }

        GithubUserResponse githubUser = response.body();
        String email = fetchEmailSafe(accessToken, githubUser);

        return new OAuthUser(String.valueOf(githubUser.id()), VerificationProvider.GITHUB, email);
    }

    private String getAccessToken(String code) {
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();

        form.add("client_id", githubOAuthConfig.getClientId());
        form.add("client_secret", githubOAuthConfig.getClientSecret());
        form.add("code", code);

        HttpResponse<GithubTokenResponse> githubToken = oauthHttpClient.postFormUrlEncoded(
                githubOAuthConfig.getTokenUri(),
                form,
                () -> {
                    HttpHeaders httpHeaders = new HttpHeaders();
                    httpHeaders.set("Accept", "application/json");
                    return httpHeaders;
                    },
                GithubTokenResponse.class);

        if (!githubToken.is2xx() || githubToken.body() == null || githubToken.body().access_token() == null) {
            throw new RuntimeException("Github 토큰 요청 실패: " + githubToken.rawBody());
        }

        return githubToken.body().access_token();
    }

    private String fetchEmailSafe(String token, GithubUserResponse user) {

        if (user.email() != null && !user.email().isBlank()) {
            return user.email();
        }

        try {
            HttpResponse<GithubEmailResponse[]> githubEmailResponseList = oauthHttpClient.get(
                    githubOAuthConfig.getEmailUri(),
                    () -> {
                        HttpHeaders httpHeaders = new HttpHeaders();
                        httpHeaders.setBearerAuth(token);
                        httpHeaders.set("Accept", "application/vnd.github+json");
                        return httpHeaders;
                        },
                    GithubEmailResponse[].class);

            if (githubEmailResponseList.is2xx() && githubEmailResponseList.body() != null) {
                return Arrays.stream(githubEmailResponseList.body())
                        .filter(email -> email.primary() && email.verified())
                        .map(GithubEmailResponse::email)
                        .findFirst()
                        .orElse(null);
            }

        } catch (Exception e) {
            log.warn("Github email 조회 실패: {}", e.getMessage());
        }

        return "github_" + user.id() + "@local";
    }
}