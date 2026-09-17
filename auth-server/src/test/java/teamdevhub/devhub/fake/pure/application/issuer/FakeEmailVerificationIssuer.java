package teamdevhub.devhub.fake.pure.application.issuer;

import teamdevhub.devhub.auth.core.auth.port.out.verification.VerificationIssuer;
import teamdevhub.devhub.auth.core.auth.domain.Verification;
import teamdevhub.devhub.auth.core.auth.application.service.verification.IssuedVerification;
import teamdevhub.devhub.auth.core.auth.domain.vo.verification.VerificationMessage;
import teamdevhub.devhub.auth.core.auth.domain.vo.verification.VerificationTarget;
import teamdevhub.devhub.auth.core.auth.domain.vo.verification.VerificationType;
import teamdevhub.devhub.shared.time.TimeProvider;

public class FakeEmailVerificationIssuer implements VerificationIssuer {

    private final VerificationType verificationType;
    private final TimeProvider timeProvider;
    private final String fixedCode;

    public FakeEmailVerificationIssuer(
            VerificationType supportedType,
            String fixedCode,
            TimeProvider timeProvider
    ) {
        this.verificationType = supportedType;
        this.fixedCode = fixedCode;
        this.timeProvider = timeProvider;
    }

    @Override
    public boolean supports(VerificationTarget verificationTarget) {
        return verificationTarget.verificationType() == verificationType;
    }

    @Override
    public IssuedVerification issue(VerificationTarget verificationTarget) {
        VerificationMessage verificationMessage = new VerificationMessage(fixedCode, timeProvider.now().plusMinutes(5));
        Verification verification = Verification.issue(verificationTarget, verificationMessage);

        return IssuedVerification.withVerificationMessage(verification, verificationMessage);
    }
}
