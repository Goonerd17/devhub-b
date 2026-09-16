package teamdevhub.devhub.identity.core.auth.application.selector.verification;

import teamdevhub.devhub.identity.core.auth.application.service.verification.IssuedVerification;
import teamdevhub.devhub.identity.core.auth.domain.vo.verification.VerificationTarget;

public interface VerificationIssuerSelector {

    IssuedVerification issueVerification(VerificationTarget verificationTarget);
}
