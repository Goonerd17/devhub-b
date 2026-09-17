package teamdevhub.devhub.auth.outbound.auth.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import teamdevhub.devhub.auth.outbound.auth.adapter.entity.VerificationEntity;
import teamdevhub.devhub.auth.core.auth.domain.vo.verification.VerificationType;

import java.time.LocalDateTime;
import java.util.Optional;

public interface JpaVerificationRepository extends JpaRepository<VerificationEntity, Long> {

    boolean existsByVerificationTypeAndTargetValueAndExpiredAtAfterAndVerifiedFalse(VerificationType verificationType, String targetValue, LocalDateTime now);
    Optional<VerificationEntity> findTopByVerificationTypeAndTargetValueOrderByExpiredAtDesc(VerificationType verificationType, String targetValue);
    void deleteByVerificationTypeAndTargetValue(VerificationType verificationType, String targetValue);
}
