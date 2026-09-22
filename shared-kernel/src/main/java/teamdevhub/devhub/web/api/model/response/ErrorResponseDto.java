package teamdevhub.devhub.web.api.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import teamdevhub.devhub.shared.enums.ErrorCode;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponseDto {

    private String code;
    private String message;

    public static ErrorResponseDto of(ErrorCode errorCode) {
        return ErrorResponseDto.builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .build();
    }

    public static ErrorResponseDto of(String errorCode, String message) {
        return ErrorResponseDto.builder()
                .code(errorCode)
                .message(message)
                .build();
    }

    public static ErrorResponseDto of(Throwable throwable) {
        String message = resolveMessage(throwable);
        return ErrorResponseDto.builder()
                .code(ErrorCode.UNKNOWN_FAIL.getCode())
                .message(message)
                .build();
    }

    private static String resolveMessage(Throwable throwable) {
        if (throwable == null) {
            return ErrorCode.UNKNOWN_FAIL.getMessage();
        }

        if (throwable.getMessage() == null || throwable.getMessage().isBlank()) {
            return ErrorCode.UNKNOWN_FAIL.getMessage();
        }

        return throwable.getMessage();
    }
}
