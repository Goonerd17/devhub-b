package teamdevhub.devhub.shared.core.common.exception;

import teamdevhub.devhub.shared.enums.ErrorCode;
import lombok.Getter;

@Getter
public class DomainRuleException extends RuntimeException {

    private final ErrorCode errorCode;

    private DomainRuleException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public static DomainRuleException of(ErrorCode errorCode) {
        return new DomainRuleException(errorCode);
    }
}
