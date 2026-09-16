package teamdevhub.devhub.identity.outbound.auth.infrastructure.oauth.naver;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;
import teamdevhub.devhub.identity.core.auth.port.out.oauth.OAuthClient;
import teamdevhub.devhub.platform.identifier.IdentifierProvider;
import teamdevhub.devhub.identity.outbound.auth.infrastructure.oauth.OAuthHttpClient;
import teamdevhub.devhub.identity.core.auth.domain.vo.oauth.OAuthUser;
import teamdevhub.devhub.identity.outbound.auth.infrastructure.oauth.http.BearerAuthHeaderProvider;
import teamdevhub.devhub.identity.outbound.auth.infrastructure.oauth.http.DefaultHeaderProvider;
import teamdevhub.devhub.identity.outbound.auth.infrastructure.oauth.http.HttpResponse;
import teamdevhub.devhub.identity.outbound.auth.infrastructure.oauth.naver.config.NaverOAuthConfig;
import teamdevhub.devhub.identity.outbound.auth.infrastructure.oauth.naver.vo.NaverTokenResponse;
import teamdevhub.devhub.identity.outbound.auth.infrastructure.oauth.naver.vo.NaverUserResponse;
import teamdevhub.devhub.identity.core.auth.domain.VerificationProvider;

@Component
@RequiredArgsConstructor
public class NaverOAuthClientAdapter implements OAuthClient {

    private final OAuthHttpClient oauthHttpClient;
    private final IdentifierProvider identifierProvider;
    private final NaverOAuthConfig naverOAuthConfig;

    @Override
    public boolean supports(VerificationProvider verificationProvider) {
        return verificationProvider == VerificationProvider.NAVER;
    }

    @Override
    public String getAuthorizationUrl(String state) {
        return UriComponentsBuilder
                .fromUriString(naverOAuthConfig.getAuthorizationUri())
                .queryParam("response_type", "code")
                .queryParam("client_id", naverOAuthConfig.getClientId())
                .queryParam("redirect_uri", naverOAuthConfig.getRedirectUri())
                .queryParam("state", state)
                .build()
                .toUriString();
    }

    @Override
    public OAuthUser fetchUser(String code) {
        String accessToken = getAccessToken(code);

        HttpResponse<NaverUserResponse> response = oauthHttpClient.get(
                naverOAuthConfig.getUserInfoUri(),
                new BearerAuthHeaderProvider(accessToken),
                NaverUserResponse.class
        );

        if (!response.is2xx() || response.body() == null) {
            throw new RuntimeException("Naver 사용자 조회 실패: " + response.rawBody());
        }

        NaverUserResponse naverUser = response.body();

        if (naverUser.response() == null || naverUser.response().id() == null) {
            throw new RuntimeException("Naver 사용자 응답 이상: " + response.rawBody());
        }

        String email = extractEmail(naverUser);

        return new OAuthUser(
                naverUser.response().id(),
                VerificationProvider.NAVER,
                email
        );
    }

    private String getAccessToken(String code) {
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();

        form.add("grant_type", "authorization_code");
        form.add("client_id", naverOAuthConfig.getClientId());
        form.add("client_secret", naverOAuthConfig.getClientSecret());
        form.add("code", code);
        form.add("state", identifierProvider.generateIdentifier());

        HttpResponse<NaverTokenResponse> naverToken = oauthHttpClient.postFormUrlEncoded(
                naverOAuthConfig.getTokenUri(),
                form,
                new DefaultHeaderProvider(),
                NaverTokenResponse.class
        );

        if (!naverToken.is2xx() || naverToken.body() == null || naverToken.body().access_token() == null) {
            throw new RuntimeException("Naver 토큰 요청 실패: " + naverToken.rawBody());
        }

        return naverToken.body().access_token();
    }

    private String extractEmail(NaverUserResponse user) {
        if (user.response() != null && user.response().email() != null && !user.response().email().isBlank()) {
            return user.response().email();
        }

        return "naver_" + user.response().id() + "@local";
    }
}
