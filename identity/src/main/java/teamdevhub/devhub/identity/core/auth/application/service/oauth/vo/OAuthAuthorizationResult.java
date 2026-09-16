package teamdevhub.devhub.identity.core.auth.application.service.oauth.vo;

public record OAuthAuthorizationResult(String url, String state) {

    public static OAuthAuthorizationResult of(String url, String state) {
        return new OAuthAuthorizationResult(url, state);
    }
}
