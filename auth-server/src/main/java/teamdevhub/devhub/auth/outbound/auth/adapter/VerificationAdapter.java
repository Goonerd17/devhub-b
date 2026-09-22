package teamdevhub.devhub.auth.outbound.auth.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import teamdevhub.devhub.shared.outbound.common.exception.AdapterDataException;
import teamdevhub.devhub.auth.outbound.auth.adapter.mapper.VerificationMapper;
import teamdevhub.devhub.auth.outbound.auth.persistence.JpaVerificationRepository;
import teamdevhub.devhub.shared.enums.ErrorCode;
import teamdevhub.devhub.auth.core.auth.domain.Verification;
import teamdevhub.devhub.auth.core.auth.domain.vo.verification.VerificationTarget;
import teamdevhub.devhub.auth.core.auth.port.out.verification.VerificationRepository;

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
