package teamdevhub.devhub.auth.outbound.auth.infrastructure.oauth.naver.vo;

public record NaverUserResponse(
        Response response
) {
    public record Response(
            String id,
            String email
    ) {}
}