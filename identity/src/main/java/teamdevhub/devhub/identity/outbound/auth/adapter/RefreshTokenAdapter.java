package teamdevhub.devhub.identity.outbound.auth.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import teamdevhub.devhub.identity.outbound.auth.adapter.mapper.RefreshTokenMapper;
import teamdevhub.devhub.identity.outbound.auth.persistence.JpaRefreshTokenRepository;
import teamdevhub.devhub.identity.core.auth.application.service.token.RefreshToken;
import teamdevhub.devhub.identity.core.auth.port.out.token.RefreshTokenRepository;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RefreshTokenAdapter implements RefreshTokenRepository {

    private final JpaRefreshTokenRepository jpaRefreshTokenRepository;

    @Override
    public void save(RefreshToken refreshToken) {
        jpaRefreshTokenRepository.findByUserGuid(refreshToken.userGuid())
                .ifPresentOrElse(
                        refreshTokenEntity -> refreshTokenEntity.rotate(refreshToken.token()),
                        () -> jpaRefreshTokenRepository.save(RefreshTokenMapper.toEntity(refreshToken))
                );
    }

    @Override
    public Optional<RefreshToken> findByUserGuid(String userGuid) {
        return jpaRefreshTokenRepository.findByUserGuid(userGuid)
                .map(entity -> RefreshToken.of(entity.getUserGuid(), entity.getToken()));
    }

    @Override
    public void deleteByUserGuid(String userGuid) {
        jpaRefreshTokenRepository.deleteByUserGuid(userGuid);
    }
}