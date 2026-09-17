package teamdevhub.devhub.auth.outbound.auth.infrastructure.oauth.kakao.vo;

public record KakaoUserResponse(
        Long id,
        KakaoAccount kakao_account
) {
    public record KakaoAccount(
            String email
    ) {}
}