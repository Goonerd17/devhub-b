package teamdevhub.devhub.identity.core.auth.port.out.verification;

import teamdevhub.devhub.identity.core.auth.domain.vo.verification.VerificationTarget;
import teamdevhub.devhub.identity.core.auth.application.service.verification.IssuedVerification;

public interface VerificationIssuer {

    boolean supports(VerificationTarget verificationTarget);
    IssuedVerification issue(VerificationTarget verificationTarget);
}
