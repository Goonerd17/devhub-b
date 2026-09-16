package teamdevhub.devhub.identity.core.auth.port.in.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teamdevhub.devhub.identity.core.auth.application.service.verification.IssuedVerification;
import teamdevhub.devhub.identity.core.auth.port.in.command.verification.ConfirmVerificationCommand;
import teamdevhub.devhub.identity.core.auth.port.in.command.verification.IssueVerificationCommand;
import teamdevhub.devhub.identity.core.auth.port.in.usecase.verification.VerificationUseCase;
import teamdevhub.devhub.notification.api.VerificationNotificationSender;

@Service
@Transactional
@RequiredArgsConstructor
public class VerificationFacade {

    private final VerificationUseCase verificationUseCase;
    private final VerificationNotificationSender notificationSender;

    public void issueVerification(IssueVerificationCommand issueVerificationCommand) {
        IssuedVerification issuedVerification = verificationUseCase.issueVerification(issueVerificationCommand);
        notificationSender.sendVerification(
                issuedVerification.verification().getVerificationTarget().value(),
                issuedVerification.verificationMessage().code());
    }

    public void confirmVerification(ConfirmVerificationCommand confirmVerificationCommand) {
        verificationUseCase.confirmVerification(confirmVerificationCommand);
    }
}
