package teamdevhub.devhub.auth.core.auth.domain.vo.verification;

public record VerificationTarget(VerificationType type, String value) {
    public static VerificationTarget of(VerificationType type, String value) {
        return new VerificationTarget(type, value);
    }
}
