package teamdevhub.devhub.identity.outbound.auth.infrastructure.oauth.kakao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;
import teamdevhub.devhub.identity.core.auth.port.out.oauth.OAuthClient;
import teamdevhub.devhub.identity.outbound.auth.infrastructure.oauth.OAuthHttpClient;
import teamdevhub.devhub.identity.core.auth.domain.vo.oauth.OAuthUser;
import teamdevhub.devhub.identity.outbound.auth.infrastructure.oauth.http.DefaultHeaderProvider;
import teamdevhub.devhub.identity.outbound.auth.infrastructure.oauth.kakao.config.KakaoOAuthConfig;
import teamdevhub.devhub.identity.outbound.auth.infrastructure.oauth.kakao.vo.KakaoTokenResponse;
import teamdevhub.devhub.identity.outbound.auth.infrastructure.oauth.kakao.vo.KakaoUserResponse;
import teamdevhub.devhub.identity.outbound.auth.infrastructure.oauth.http.BearerAuthHeaderProvider;
import teamdevhub.devhub.identity.outbound.auth.infrastructure.oauth.http.HttpResponse;
import teamdevhub.devhub.identity.core.auth.domain.VerificationProvider;

import java.net.URI;

@Component
@RequiredArgsConstructor
public class KakaoOAuthClientAdapter implements OAuthClient {

    private final OAuthHttpClient oauthHttpClient;
    private final KakaoOAuthConfig kakaoOAuthConfig;

    @Override
    public boolean supports(VerificationProvider verificationProvider) {
        return verificationProvider == VerificationProvider.KAKAO;
    }

    @Override
    public String getAuthorizationUrl(String state) {
        return UriComponentsBuilder
                .fromUri(URI.create(kakaoOAuthConfig.getAuthorizationUri()))
                .queryParam("client_id", kakaoOAuthConfig.getClientId())
                .queryParam("redirect_uri", kakaoOAuthConfig.getRedirectUri())
                .queryParam("response_type", "code")
                .queryParam("state", state)
                .build()
                .toUriString();
    }

    @Override
    public OAuthUser fetchUser(String code) {
        String accessToken = getAccessToken(code);

        HttpResponse<KakaoUserResponse> response = oauthHttpClient.get(kakaoOAuthConfig.getUserInfoUri(), new BearerAuthHeaderProvider(accessToken), KakaoUserResponse.class);

        if (!response.is2xx() || response.body() == null) {
            throw new RuntimeException("Kakao 사용자 조회 실패: " + response.rawBody());
        }

        KakaoUserResponse kakaoUser = response.body();
        String email = (kakaoUser.kakao_account() != null) ? kakaoUser.kakao_account().email() : null;

        if (email == null || email.isBlank()) {
            email = "kakao_" + kakaoUser.id() + "@local";
        }

        return new OAuthUser(String.valueOf(kakaoUser.id()), VerificationProvider.KAKAO, email);
    }

    private String getAccessToken(String code) {
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();

        form.add("grant_type", "authorization_code");
        form.add("client_id", kakaoOAuthConfig.getClientId());
        form.add("client_secret", kakaoOAuthConfig.getClientSecret());
        form.add("redirect_uri", kakaoOAuthConfig.getRedirectUri());
        form.add("code", code);

        HttpResponse<KakaoTokenResponse> kakaoToken = oauthHttpClient.postFormUrlEncoded(kakaoOAuthConfig.getTokenUri(), form, new DefaultHeaderProvider(), KakaoTokenResponse.class);

        if (!kakaoToken.is2xx() || kakaoToken.body() == null || kakaoToken.body().access_token() == null) {
            throw new RuntimeException("Kakao 토큰 요청 실패: " + kakaoToken.rawBody());
        }

        return kakaoToken.body().access_token();
    }
}