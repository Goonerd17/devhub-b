package teamdevhub.devhub.fake.pure.application.port.in.usecase.auth.verification;

import teamdevhub.devhub.auth.core.auth.application.service.verification.IssuedVerification;
import teamdevhub.devhub.auth.core.auth.domain.vo.verification.VerificationMessage;
import teamdevhub.devhub.auth.core.auth.domain.vo.verification.VerificationType;
import teamdevhub.devhub.shared.time.TimeProvider;
import teamdevhub.devhub.auth.core.auth.domain.Verification;
import teamdevhub.devhub.auth.core.auth.domain.vo.verification.VerificationTarget;
import teamdevhub.devhub.fake.pure.application.provider.FakeTimeProvider;
import teamdevhub.devhub.auth.core.auth.port.in.usecase.verification.VerificationUseCase;
import teamdevhub.devhub.auth.core.auth.port.in.command.verification.ConfirmVerificationCommand;
import teamdevhub.devhub.auth.core.auth.port.in.command.verification.IssueVerificationCommand;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static teamdevhub.devhub.constant.UserTestConstant.TEST_EMAIL_CODE;
import static teamdevhub.devhub.constant.UserTestConstant.TEST_EMAIL_1;

public class FakeVerificationUseCase implements VerificationUseCase {

    private final Map<String, Verification> store = new HashMap<>();
    private final TimeProvider timeProvider = new FakeTimeProvider(LocalDateTime.of(2025, 1, 1, 12, 0));

    public FakeVerificationUseCase() {
        VerificationTarget verificationTarget = VerificationTarget.of(VerificationType.EMAIL, TEST_EMAIL_1);
        VerificationMessage verificationMessage = new VerificationMessage(TEST_EMAIL_CODE, timeProvider.now().plusMinutes(5));
        Verification verification = Verification.issue(verificationTarget, verificationMessage);
        verification.confirm(TEST_EMAIL_CODE, timeProvider.now());
        store.put(verificationTarget.value(), verification);
    }

    @Override
    public IssuedVerification issueVerification(IssueVerificationCommand issueVerificationCommand) {
        VerificationTarget verificationTarget = issueVerificationCommand.verificationTarget();
        VerificationMessage verificationMessage = new VerificationMessage(TEST_EMAIL_CODE, timeProvider.now().plusMinutes(5));
        Verification verification = Verification.issue(verificationTarget, verificationMessage);
        store.put(verificationTarget.value(), verification);
        return new IssuedVerification(verification, verificationMessage);
    }

    @Override
    public void confirmVerification(ConfirmVerificationCommand confirmVerificationCommand) {
        VerificationTarget verificationTarget = confirmVerificationCommand.verificationTarget();
        Verification verification = store.get(verificationTarget.value());
        verification.confirm(confirmVerificationCommand.code(), timeProvider.now());
        store.put(verificationTarget.value(), verification);
    }

    @Override
    public void assertAllowed(VerificationTarget verificationTarget) {
        Verification verification = store.get(verificationTarget.value());
        verification.assertValid(timeProvider.now());
    }

    @Override
    public void consume(VerificationTarget verificationTarget) {
        store.remove(verificationTarget.value());
    }

    public void putUnverified(VerificationTarget verificationTarget) {
        VerificationMessage verificationMessage = new VerificationMessage(TEST_EMAIL_CODE, timeProvider.now().plusMinutes(5));
        Verification verification = Verification.issue(verificationTarget, verificationMessage);
        store.put(verificationTarget.value(), verification);
    }
}
