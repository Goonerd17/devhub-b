package teamdevhub.devhub.auth.core.auth.application.selector.verification;

import teamdevhub.devhub.auth.core.auth.application.service.verification.IssuedVerification;
import teamdevhub.devhub.auth.core.auth.domain.vo.verification.VerificationTarget;

public interface VerificationIssuerSelector {

    IssuedVerification issueVerification(VerificationTarget verificationTarget);
}
