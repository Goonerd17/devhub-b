package teamdevhub.devhub.shared.core.common.exception;

import teamdevhub.devhub.shared.shared.enums.ErrorCode;
import lombok.Getter;

@Getter
public class BusinessRuleException extends RuntimeException {

    private final ErrorCode errorCode;

    private BusinessRuleException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public static BusinessRuleException of(ErrorCode errorCode) {
        return new BusinessRuleException(errorCode);
    }
}