package teamdevhub.devhub.identity.core.auth.port.in.usecase.verification;

import teamdevhub.devhub.identity.core.auth.application.service.verification.IssuedVerification;
import teamdevhub.devhub.identity.core.auth.domain.vo.verification.VerificationTarget;
import teamdevhub.devhub.identity.core.auth.port.in.command.verification.ConfirmVerificationCommand;
import teamdevhub.devhub.identity.core.auth.port.in.command.verification.IssueVerificationCommand;

public interface VerificationUseCase {

    IssuedVerification issueVerification(IssueVerificationCommand issueVerificationCommand);
    void confirmVerification(ConfirmVerificationCommand confirmVerificationCommand);
    void assertAllowed(VerificationTarget verificationTarget);
    void consume(VerificationTarget verificationTarget);
}
