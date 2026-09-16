package teamdevhub.devhub.fake.pure.application.port.out.auth.verification;

import teamdevhub.devhub.platform.core.common.exception.BusinessRuleException;
import teamdevhub.devhub.platform.shared.enums.ErrorCode;
import teamdevhub.devhub.identity.core.auth.domain.Verification;
import teamdevhub.devhub.identity.core.auth.domain.vo.verification.VerificationTarget;
import teamdevhub.devhub.identity.core.auth.port.out.verification.VerificationRepository;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class FakeVerificationRepository implements VerificationRepository {

    private final Map<String, Verification> store = new HashMap<>();
    private long sequence = 1L;

    @Override
    public boolean existsUnverifiedAndNotExpired(VerificationTarget verificationTarget, LocalDateTime now) {
        return false;
    }

    @Override
    public void save(Verification verification) {
        if (verification.getId() == null) {
            Verification savedVerification = Verification.of(
                    sequence++,
                    verification.getVerificationTarget(),
                    verification.getCode(),
                    verification.getExpiredAt(),
                    verification.isVerified()
            );
            store.put(savedVerification.getVerificationTarget().value(), savedVerification);
        } else {
            store.put(verification.getVerificationTarget().value(), verification);
        }
    }

    @Override
    public Verification findByVerificationTarget(VerificationTarget verificationTarget) {
        Verification verification = store.get(verificationTarget.value());
        if (verification == null) {
            throw BusinessRuleException.of(ErrorCode.VERIFICATION_NOT_EXISTED);
        }
        return verification;
    }

    @Override
    public void deleteByVerificationTarget(VerificationTarget verificationTarget) {
        store.remove(verificationTarget.value());
    }
}
