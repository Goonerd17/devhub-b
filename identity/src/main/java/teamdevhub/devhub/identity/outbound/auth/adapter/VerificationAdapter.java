package teamdevhub.devhub.identity.outbound.auth.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import teamdevhub.devhub.platform.outbound.common.exception.AdapterDataException;
import teamdevhub.devhub.identity.outbound.auth.adapter.mapper.VerificationMapper;
import teamdevhub.devhub.identity.outbound.auth.persistence.JpaVerificationRepository;
import teamdevhub.devhub.platform.shared.enums.ErrorCode;
import teamdevhub.devhub.identity.core.auth.domain.Verification;
import teamdevhub.devhub.identity.core.auth.domain.vo.verification.VerificationTarget;
import teamdevhub.devhub.identity.core.auth.port.out.verification.VerificationRepository;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class VerificationAdapter implements VerificationRepository {

    private final JpaVerificationRepository jpaVerificationRepository;

    @Override
    public boolean existsUnverifiedAndNotExpired(VerificationTarget verificationTarget, LocalDateTime now) {
        return jpaVerificationRepository.existsByVerificationTypeAndTargetValueAndExpiredAtAfterAndVerifiedFalse(
                        verificationTarget.verificationType(),
                        verificationTarget.value(),
                        now
        );
    }

    @Override
    public void save(Verification verification) {
        jpaVerificationRepository.save(VerificationMapper.toEntity(verification));
    }

    @Override
    public Verification findByVerificationTarget(VerificationTarget verificationTarget) {
        return jpaVerificationRepository.findTopByVerificationTypeAndTargetValueOrderByExpiredAtDesc(verificationTarget.verificationType(), verificationTarget.value())
                .map(VerificationMapper::toDomain)
                .orElseThrow(() -> AdapterDataException.of(ErrorCode.VERIFICATION_NOT_EXISTED));
    }

    @Override
    public void deleteByVerificationTarget(VerificationTarget verificationTarget) {
        jpaVerificationRepository.deleteByVerificationTypeAndTargetValue(verificationTarget.verificationType(), verificationTarget.value());
    }
}