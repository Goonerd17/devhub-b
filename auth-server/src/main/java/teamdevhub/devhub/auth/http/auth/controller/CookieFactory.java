package teamdevhub.devhub.auth.http.auth.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
public class CookieFactory {

    private static final String REFRESH_COOKIE_NAME = "refreshToken";
    private static final String OAUTH_STATE_COOKIE_NAME = "oauthState";
    private static final String SAME_SITE = "Lax";
    private static final String PATH = "/api/auth/reissue";
    private static final String OAUTH_PATH = "/api/auth/oauth";
    private static final int MAX_AGE_SECONDS = 14 * 24 * 60 * 60;
    private static final int OAUTH_STATE_MAX_AGE_SECONDS = 300;

    @Value("${app.cookie.secure:false}")
    private boolean secureCookie;

    public ResponseCookie createRefreshTokenCookie(String refreshToken) {
        return ResponseCookie.from(REFRESH_COOKIE_NAME, refreshToken)
                .httpOnly(true)
                .secure(secureCookie)
                .sameSite(SAME_SITE)
                .path(PATH)
                .maxAge(MAX_AGE_SECONDS)
                .build();
    }

    public ResponseCookie expireRefreshTokenCookie() {
        return ResponseCookie.from(REFRESH_COOKIE_NAME, "")
                .httpOnly(true)
                .secure(secureCookie)
                .sameSite(SAME_SITE)
                .path(PATH)
                .maxAge(0)
                .build();
    }

    public ResponseCookie createOAuthStateCookie(String state) {
        return ResponseCookie.from(OAUTH_STATE_COOKIE_NAME, state)
                .httpOnly(true)
                .secure(secureCookie)
                .sameSite(SAME_SITE)
                .path(OAUTH_PATH)
                .maxAge(OAUTH_STATE_MAX_AGE_SECONDS)
                .build();
    }

    public ResponseCookie expireOAuthStateCookie() {
        return ResponseCookie.from(OAUTH_STATE_COOKIE_NAME, "")
                .httpOnly(true)
                .secure(secureCookie)
                .sameSite(SAME_SITE)
                .path(OAUTH_PATH)
                .maxAge(0)
                .build();
    }
}
