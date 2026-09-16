package teamdevhub.devhub.identity.outbound.auth.infrastructure.oauth.naver.vo;

public record NaverTokenResponse(
        String access_token,
        String token_type,
        String refresh_token,
        Integer expires_in
) {}