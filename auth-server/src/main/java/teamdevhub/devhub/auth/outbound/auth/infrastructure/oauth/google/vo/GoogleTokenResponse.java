package teamdevhub.devhub.auth.outbound.auth.infrastructure.oauth.google.vo;

public record GoogleTokenResponse(
        String access_token,
        String token_type,
        Integer expires_in,
        String scope
) {}
