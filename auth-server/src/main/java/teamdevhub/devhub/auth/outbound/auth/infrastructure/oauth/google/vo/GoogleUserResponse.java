package teamdevhub.devhub.auth.outbound.auth.infrastructure.oauth.google.vo;

public record GoogleUserResponse(
        String id,
        String email,
        Boolean verified_email,
        String name
) {}