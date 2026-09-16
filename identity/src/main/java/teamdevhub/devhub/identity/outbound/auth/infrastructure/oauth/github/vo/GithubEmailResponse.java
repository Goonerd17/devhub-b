package teamdevhub.devhub.identity.outbound.auth.infrastructure.oauth.github.vo;

public record GithubEmailResponse(
        String email,
        boolean primary,
        boolean verified
) {}