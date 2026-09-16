package teamdevhub.devhub.identity.core.auth.application.service.verification;

import lombok.Builder;
import teamdevhub.devhub.identity.core.auth.domain.Verification;
import teamdevhub.devhub.identity.core.auth.domain.vo.verification.VerificationMessage;

import java.util.Optional;

@Builder
public record IssuedVerification(Verification verification, VerificationMessage verificationMessage) {

    public static IssuedVerification withVerificationMessage(Verification verification, VerificationMessage verificationMessage) {
        return new IssuedVerification(verification, verificationMessage);
    }

    public Optional<VerificationMessage> getVerificationMessage() {
        return Optional.ofNullable(verificationMessage);
    }
}