package teamdevhub.devhub.auth.core.auth.port.out.token;

import teamdevhub.devhub.auth.core.auth.application.service.token.RefreshToken;

import java.util.Optional;

public interface RefreshTokenRepository {

    void save(RefreshToken refreshToken);
    Optional<RefreshToken> findByUserGuid(String userGuid);
    void deleteByUserGuid(String userGuid);
}