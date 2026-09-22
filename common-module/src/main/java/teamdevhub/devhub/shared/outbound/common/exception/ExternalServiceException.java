package teamdevhub.devhub.shared.outbound.common.exception;

import lombok.Getter;
import teamdevhub.devhub.shared.enums.ErrorCode;

@Getter
public class ExternalServiceException extends RuntimeException {

    private final ErrorCode errorCode;

    private ExternalServiceException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public static ExternalServiceException of(ErrorCode errorCode) {
        return new ExternalServiceException(errorCode);
    }
}
