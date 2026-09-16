package teamdevhub.devhub.identity.outbound.auth.infrastructure.oauth.github.vo;

public record GithubUserResponse(
        Long id,
        String login,
        String email
) {}