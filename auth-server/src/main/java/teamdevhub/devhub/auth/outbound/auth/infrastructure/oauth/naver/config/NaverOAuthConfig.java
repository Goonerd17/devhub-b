package teamdevhub.devhub.auth.outbound.auth.infrastructure.oauth.naver.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import teamdevhub.devhub.auth.outbound.auth.infrastructure.oauth.OAuthProviderConfig;

@Getter
@Component
public class NaverOAuthConfig implements OAuthProviderConfig {

    @Value("${oauth.naver.client-id}")
    private String clientId;

    @Value("${oauth.naver.client-secret}")
    private String clientSecret;

    @Value("${oauth.naver.redirect-uri}")
    private String redirectUri;

    @Override
    public String getAuthorizationUri() {
        return "https://nid.naver.com/oauth2.0/authorize";
    }

    @Override
    public String getTokenUri() {
        return "https://nid.naver.com/oauth2.0/token";
    }

    @Override
    public String getUserInfoUri() {
        return "https://openapi.naver.com/v1/nid/me";
    }
}