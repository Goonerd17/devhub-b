package teamdevhub.devhub.identity.core.auth.application.issuer;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import teamdevhub.devhub.identity.core.auth.port.out.verification.VerificationIssuer;
import teamdevhub.devhub.identity.core.auth.application.service.verification.IssuedVerification;
import teamdevhub.devhub.platform.time.TimeProvider;
import teamdevhub.devhub.identity.core.auth.port.out.verification.VerificationCodeProvider;
import teamdevhub.devhub.identity.core.auth.domain.Verification;
import teamdevhub.devhub.identity.core.auth.domain.vo.verification.VerificationMessage;
import teamdevhub.devhub.identity.core.auth.domain.vo.verification.VerificationTarget;
import teamdevhub.devhub.identity.core.auth.domain.vo.verification.VerificationType;

@Component
@RequiredArgsConstructor
public class EmailVerificationIssuer implements VerificationIssuer {

    private final VerificationCodeProvider verificationCodeProvider;
    private final TimeProvider timeProvider;

    @Override
    public boolean supports(VerificationTarget verificationTarget) {
        return verificationTarget.verificationType() == VerificationType.EMAIL;
    }

    @Override
    public IssuedVerification issue(VerificationTarget verificationTarget) {
        String verificationCode = verificationCodeProvider.generateVerificationCode();
        VerificationMessage verificationMessage = new VerificationMessage(verificationCode, timeProvider.now().plusMinutes(5));
        Verification verification = Verification.issue(verificationTarget, verificationMessage);

        return IssuedVerification.withVerificationMessage(verification, verificationMessage);
    }
}
