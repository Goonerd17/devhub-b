package teamdevhub.devhub.identity.outbound.auth.infrastructure.oauth.kakao.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import teamdevhub.devhub.identity.outbound.auth.infrastructure.oauth.OAuthProviderConfig;

@Getter
@Component
public class KakaoOAuthConfig implements OAuthProviderConfig {

    @Value("${oauth.kakao.client-id}")
    private String clientId;

    @Value("${oauth.kakao.client-secret}")
    private String clientSecret;

    @Value("${oauth.kakao.redirect-uri}")
    private String redirectUri;

    @Override
    public String getAuthorizationUri() {
        return "https://kauth.kakao.com/oauth/authorize";
    }

    @Override
    public String getTokenUri() {
        return "https://kauth.kakao.com/oauth/token";
    }

    @Override
    public String getUserInfoUri() {
        return "https://kapi.kakao.com/v2/user/me";
    }
}