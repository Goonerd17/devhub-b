package teamdevhub.devhub.media.http;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import teamdevhub.devhub.shared.core.common.exception.BusinessRuleException;
import teamdevhub.devhub.shared.core.common.exception.DomainRuleException;
import teamdevhub.devhub.shared.outbound.common.exception.AdapterDataException;
import teamdevhub.devhub.shared.outbound.common.exception.ExternalServiceException;
import teamdevhub.devhub.web.api.model.response.DataApiResponseDto;

@RestControllerAdvice(basePackages = "teamdevhub.devhub.media.http")
public class MediaExceptionHandler {
    @ExceptionHandler(DomainRuleException.class)
    ResponseEntity<DataApiResponseDto<?>> domain(DomainRuleException error) {
        return ResponseEntity.ok(DataApiResponseDto.failureWithoutData(error.getErrorCode()));
    }

    @ExceptionHandler(BusinessRuleException.class)
    ResponseEntity<DataApiResponseDto<?>> business(BusinessRuleException error) {
        return ResponseEntity.ok(DataApiResponseDto.failureWithoutData(error.getErrorCode()));
    }

    @ExceptionHandler(AdapterDataException.class)
    ResponseEntity<DataApiResponseDto<?>> adapter(AdapterDataException error) {
        return ResponseEntity.ok(DataApiResponseDto.failureWithoutData(error.getErrorCode()));
    }

    @ExceptionHandler(ExternalServiceException.class)
    ResponseEntity<DataApiResponseDto<?>> external(ExternalServiceException error) {
        return ResponseEntity.ok(DataApiResponseDto.failureWithoutData(error.getErrorCode()));
    }

    @ExceptionHandler(Exception.class)
    ResponseEntity<DataApiResponseDto<?>> unexpected(Exception error) {
        return ResponseEntity.badRequest().body(DataApiResponseDto.failureFromThrowable(error));
    }
}
