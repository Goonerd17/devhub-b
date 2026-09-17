package teamdevhub.devhub.auth.outbound.auth.adapter.mapper;

import teamdevhub.devhub.auth.outbound.auth.adapter.entity.RefreshTokenEntity;
import teamdevhub.devhub.auth.core.auth.application.service.token.RefreshToken;

public class RefreshTokenMapper {

    public static RefreshTokenEntity toEntity(RefreshToken refreshToken) {
        return RefreshTokenEntity.of(
                refreshToken.userGuid(),
                refreshToken.token()
        );
    }

    public static RefreshToken toDomain(RefreshTokenEntity refreshTokenEntity) {
        return RefreshToken.of(
                refreshTokenEntity.getUserGuid(),
                refreshTokenEntity.getToken()
        );
    }
}
