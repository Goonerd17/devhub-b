package teamdevhub.devhub.identity.outbound.auth.infrastructure.oauth.kakao.vo;

public record KakaoTokenResponse(
        String access_token,
        String token_type,
        Integer expires_in
) {}
