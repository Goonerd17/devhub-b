package teamdevhub.devhub.web.shared.exception;

import teamdevhub.devhub.web.api.web.model.response.DataApiResponseDto;
import teamdevhub.devhub.platform.core.common.exception.BusinessRuleException;
import teamdevhub.devhub.platform.core.common.exception.DomainRuleException;
import teamdevhub.devhub.platform.outbound.common.exception.AdapterDataException;
import teamdevhub.devhub.platform.outbound.common.exception.ExternalServiceException;
import teamdevhub.devhub.platform.shared.enums.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger errorFileLogger = LoggerFactory.getLogger("ERROR_FILE");

    private void logException(Exception e) {
        String traceId = MDC.get("traceId");
        errorFileLogger.error("[{}] -----GlobalExceptionHandler-----: {}", traceId, e.getMessage(), e);
        log.error("[{}] -----GlobalExceptionHandler-----: {}", traceId, e.getMessage(), e);
    }

    @ExceptionHandler(DomainRuleException.class)
    @ResponseStatus(OK)
    public ResponseEntity<DataApiResponseDto<?>> handleDomainException(DomainRuleException e) {
        logException(e);
        return ResponseEntity.ok()
                .body(DataApiResponseDto.failureWithoutData(e.getErrorCode()));
    }

    @ExceptionHandler(BusinessRuleException.class)
    @ResponseStatus(OK)
    public ResponseEntity<DataApiResponseDto<?>> handleBusinessRuleException(BusinessRuleException e) {
        logException(e);
        return ResponseEntity.ok()
                .body(DataApiResponseDto.failureWithoutData(e.getErrorCode()));
    }

    @ExceptionHandler(AdapterDataException.class)
    @ResponseStatus(OK)
    public ResponseEntity<DataApiResponseDto<?>> handleAdapterDataException(AdapterDataException e) {
        logException(e);
        return ResponseEntity.ok()
                .body(DataApiResponseDto.failureWithoutData(e.getErrorCode()));
    }

    @ExceptionHandler(ExternalServiceException.class)
    @ResponseStatus(OK)
    public ResponseEntity<DataApiResponseDto<?>> handleExternalServiceException(ExternalServiceException e) {
        logException(e);
        return ResponseEntity.ok()
                .body(DataApiResponseDto.failureWithoutData(e.getErrorCode()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(OK)
    public ResponseEntity<DataApiResponseDto<?>> handleValidationException(MethodArgumentNotValidException methodArgumentNotValidException) {
        logException(methodArgumentNotValidException);
        String message = methodArgumentNotValidException.getBindingResult()
                .getFieldErrors()
                .stream()
                .findFirst()
                .map(FieldError::getDefaultMessage)
                .orElse(ErrorCode.VALIDATION_FAIL.getMessage());

        return ResponseEntity.ok()
                .body(DataApiResponseDto.failureWithMessage(ErrorCode.VALIDATION_FAIL, message)
        );
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(BAD_REQUEST)
    public ResponseEntity<DataApiResponseDto<?>> handleException(Exception e) {
        logException(e);
        return ResponseEntity.badRequest()
                .body(DataApiResponseDto.failureFromThrowable(e));
    }
}