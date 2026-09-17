package teamdevhub.devhub.shared.outbound.common.exception;

import teamdevhub.devhub.shared.shared.enums.ErrorCode;
import lombok.Getter;

@Getter
public class AuthRuleException extends RuntimeException {

    private final ErrorCode errorCode;

    private AuthRuleException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public static AuthRuleException of(ErrorCode errorCode) {
        return new AuthRuleException(errorCode);
    }
}