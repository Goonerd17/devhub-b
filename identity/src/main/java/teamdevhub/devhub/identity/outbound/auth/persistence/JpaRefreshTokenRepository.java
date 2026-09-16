package teamdevhub.devhub.identity.outbound.auth.persistence;

import teamdevhub.devhub.identity.outbound.auth.adapter.entity.RefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaRefreshTokenRepository extends JpaRepository<RefreshTokenEntity, Long> {

    Optional<RefreshTokenEntity> findByUserGuid(String userGuid);
    void deleteByUserGuid(String userGuid);
}