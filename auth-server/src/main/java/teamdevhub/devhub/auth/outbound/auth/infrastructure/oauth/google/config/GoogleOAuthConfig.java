package teamdevhub.devhub.auth.outbound.auth.infrastructure.oauth.google.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import teamdevhub.devhub.auth.outbound.auth.infrastructure.oauth.OAuthProviderConfig;

@Getter
@Component
public class GoogleOAuthConfig implements OAuthProviderConfig {

    @Value("${oauth.google.client-id}")
    private String clientId;

    @Value("${oauth.google.client-secret}")
    private String clientSecret;

    @Value("${oauth.google.redirect-uri}")
    private String redirectUri;

    @Override
    public String getAuthorizationUri() {
        return "https://accounts.google.com/o/oauth2/v2/auth";
    }

    @Override
    public String getTokenUri() {
        return "https://oauth2.googleapis.com/token";
    }

    @Override
    public String getUserInfoUri() {
        return "https://www.googleapis.com/oauth2/v2/userinfo";
    }
}