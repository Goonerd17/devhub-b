package teamdevhub.devhub.auth.core.auth.port.out.verification;

import teamdevhub.devhub.auth.core.auth.domain.vo.verification.VerificationTarget;
import teamdevhub.devhub.auth.core.auth.application.service.verification.IssuedVerification;

public interface VerificationIssuer {

    boolean supports(VerificationTarget verificationTarget);
    IssuedVerification issue(VerificationTarget verificationTarget);
}
