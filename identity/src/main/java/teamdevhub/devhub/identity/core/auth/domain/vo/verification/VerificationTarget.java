package teamdevhub.devhub.identity.core.auth.domain.vo.verification;

import lombok.Builder;
import teamdevhub.devhub.platform.shared.enums.ErrorCode;
import teamdevhub.devhub.platform.core.common.exception.DomainRuleException;

@Builder
public record VerificationTarget(VerificationType verificationType, String value) {

    public static VerificationTarget of(VerificationType verificationType, String value) {
        validate(verificationType, value);
        return new VerificationTarget(verificationType, value);
    }

    public static VerificationTarget fromEntity(VerificationType verificationType, String value) {
        return new VerificationTarget(verificationType, value);
    }

    private static void validate(VerificationType verificationType, String value) {
        validateRequired(verificationType, value);
        verificationType.validate(value);
    }

    private static void validateRequired(VerificationType verificationType, String value) {
        if (verificationType == null) {
            throw DomainRuleException.of(ErrorCode.VERIFICATION_TYPE_INVALID);
        }
        if (value == null) {
            throw DomainRuleException.of(ErrorCode.VERIFICATION_VALUE_REQUIRED);
        }
    }
}