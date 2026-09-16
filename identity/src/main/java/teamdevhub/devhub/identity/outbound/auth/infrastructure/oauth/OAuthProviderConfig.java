package teamdevhub.devhub.identity.outbound.auth.infrastructure.oauth;

public interface OAuthProviderConfig {

    String getClientId();
    String getClientSecret();
    String getRedirectUri();
    String getAuthorizationUri();
    String getTokenUri();
    String getUserInfoUri();
}