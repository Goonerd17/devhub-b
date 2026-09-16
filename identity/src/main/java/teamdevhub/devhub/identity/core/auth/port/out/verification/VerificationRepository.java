package teamdevhub.devhub.identity.core.auth.port.out.verification;

import teamdevhub.devhub.identity.core.auth.domain.Verification;
import teamdevhub.devhub.identity.core.auth.domain.vo.verification.VerificationTarget;

import java.time.LocalDateTime;

public interface VerificationRepository {

    boolean existsUnverifiedAndNotExpired(VerificationTarget verificationTarget, LocalDateTime now);
    void save(Verification verification);
    Verification findByVerificationTarget(VerificationTarget verificationTarget);
    void deleteByVerificationTarget(VerificationTarget verificationTarget);
}
