package teamdevhub.devhub.auth.outbound.auth.infrastructure.oauth.github.vo;

public record GithubUserResponse(
        Long id,
        String login,
        String email
) {}