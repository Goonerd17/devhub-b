package teamdevhub.devhub.web.api.web.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import teamdevhub.devhub.platform.shared.enums.ErrorCode;
import teamdevhub.devhub.web.shared.enums.SuccessCode;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DataApiResponseDto<T> {

    @JsonProperty("success")
    private boolean isSuccess;
    private String code;
    private T data;
    private ErrorResponseDto error;

    public static <T> DataApiResponseDto<T> successWithData(SuccessCode successCode, T data) {
        return DataApiResponseDto.<T>builder()
                .isSuccess(true)
                .code(successCode.getCode())
                .data(data)
                .build();
    }

    public static DataApiResponseDto<Void> successWithoutData(SuccessCode successCode) {
        return DataApiResponseDto.<Void>builder()
                .isSuccess(true)
                .code(successCode.getCode())
                .build();
    }
    
    public static <T> DataApiResponseDto<T> failureWithoutData(ErrorCode errorCode) {
        return DataApiResponseDto.<T>builder()
                .isSuccess(false)
                .code(errorCode.getCode())
                .error(ErrorResponseDto.of(errorCode))
                .build();
    }

    public static DataApiResponseDto<?> failureWithMessage(ErrorCode errorCode, String message) {
        return DataApiResponseDto.builder()
                .isSuccess(false)
                .code(errorCode.getCode())
                .error(ErrorResponseDto.of(errorCode.getCode(), message))
                .build();
    }

    public static <T> DataApiResponseDto<T> failureFromThrowable(Throwable throwable) {
        return DataApiResponseDto.<T>builder()
                .isSuccess(false)
                .code(ErrorCode.UNKNOWN_FAIL.getCode())
                .error(ErrorResponseDto.of(throwable))
                .build();
    }
}