package teamdevhub.devhub.identity.core.auth.application.service.verification;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teamdevhub.devhub.identity.core.auth.application.selector.verification.VerificationIssuerSelector;
import teamdevhub.devhub.identity.core.auth.domain.Verification;
import teamdevhub.devhub.identity.core.auth.domain.vo.verification.VerificationTarget;
import teamdevhub.devhub.identity.core.auth.port.in.command.verification.ConfirmVerificationCommand;
import teamdevhub.devhub.identity.core.auth.port.in.command.verification.IssueVerificationCommand;
import teamdevhub.devhub.identity.core.auth.port.in.usecase.verification.VerificationUseCase;
import teamdevhub.devhub.identity.core.auth.port.out.verification.VerificationRepository;
import teamdevhub.devhub.platform.core.common.exception.BusinessRuleException;
import teamdevhub.devhub.platform.time.TimeProvider;
import teamdevhub.devhub.platform.shared.enums.ErrorCode;

@Service
@Transactional
@RequiredArgsConstructor
public class VerificationService implements VerificationUseCase {

    private final TimeProvider timeProvider;
    private final VerificationIssuerSelector verificationIssuerSelector;
    private final VerificationRepository verificationRepository;

    @Override
    public IssuedVerification issueVerification(IssueVerificationCommand issueVerificationCommand) {
        assertIssuable(issueVerificationCommand.verificationTarget());
        IssuedVerification issuedVerification = verificationIssuerSelector.issueVerification(issueVerificationCommand.verificationTarget());
        verificationRepository.save(issuedVerification.verification());
        return issuedVerification;
    }

    @Override
    public void confirmVerification(ConfirmVerificationCommand confirmVerificationCommand) {
        Verification verification = verificationRepository.findByVerificationTarget(confirmVerificationCommand.verificationTarget());
        verification.confirm(confirmVerificationCommand.code(), timeProvider.now());
        verificationRepository.save(verification);
    }

    @Override
    public void assertAllowed(VerificationTarget verificationTarget) {
        Verification verification = verificationRepository.findByVerificationTarget(verificationTarget);
        verification.assertValid(timeProvider.now());
    }

    @Override
    public void consume(VerificationTarget verificationTarget) {
        verificationRepository.deleteByVerificationTarget(verificationTarget);
    }

    private void assertIssuable(VerificationTarget verificationTarget) {
        boolean isAlreadySent = verificationRepository.existsUnverifiedAndNotExpired(verificationTarget, timeProvider.now());
        if (isAlreadySent) {
            throw BusinessRuleException.of(ErrorCode.VERIFICATION_ALREADY_SENT);
        }
    }
}
