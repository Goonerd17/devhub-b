package teamdevhub.devhub.identity.core.auth.domain.vo.verification;

import teamdevhub.devhub.platform.shared.enums.ErrorCode;
import teamdevhub.devhub.platform.core.common.exception.DomainRuleException;

public enum VerificationType {

    EMAIL {
        @Override
        void validate(String value) {
            if (!value.matches("^[^@]+@[^@]+\\.[^@]+$")) {
                throw DomainRuleException.of(ErrorCode.INVALID_EMAIL_FORMAT);
            }
        }
    },
    SMS {
        @Override
        void validate(String value) {
            if (!value.matches("^01[0-9]{8,9}$")) {
                throw DomainRuleException.of(ErrorCode.INVALID_PHONE_NUMBER_FORMAT);
            }
        }
    },
    OTP {
        @Override
        void validate(String value) {
            if (value.isBlank()) {
                throw DomainRuleException.of(ErrorCode.OTP_BLANK);
            }
        }
    };

    abstract void validate(String value);

    public static VerificationType from(String value) {
        try {
            return VerificationType.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw DomainRuleException.of(ErrorCode.VERIFICATION_TYPE_INVALID);
        }
    }
}