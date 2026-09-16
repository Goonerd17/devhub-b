package teamdevhub.devhub.identity.core.auth.application.service;

import lombok.Builder;

@Builder
public record AuthResult(String accessToken, String refreshToken) {

    public static AuthResult of(String accessToken, String refreshToken) {
        return new AuthResult(accessToken, refreshToken);
    }

    public boolean hasRefreshToken() {
        return refreshToken != null;
    }

    public String toAuthorizationHeader() {
        return "Bearer " + accessToken;
    }
}
