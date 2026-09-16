package teamdevhub.devhub.web.api.web.model.response;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.web.api.web.model.response.ErrorResponseDto;
import teamdevhub.devhub.platform.shared.enums.ErrorCode;

import static org.assertj.core.api.Assertions.assertThat;

class ErrorResponseDtoTest {

    @Test
    @DisplayName("ErrorCode_로부터_ErrorResponseVo_를_생성한다")
    void createResponseVoFromErrorCode() {
        // given
        ErrorCode errorCode = ErrorCode.READ_FAIL;

        // when
        ErrorResponseDto errorResponseDto = ErrorResponseDto.of(errorCode);

        // then
        assertThat(errorResponseDto.getCode()).isEqualTo(errorCode.getCode());
        assertThat(errorResponseDto.getMessage()).isEqualTo(errorCode.getMessage());
    }

    @Test
    @DisplayName("코드와_메시지를_직접_지정하여_ErrorResponseVo_를_생성한다")
    void createResponseVoWithCustomCodeAndMessage() {
        // given
        String code = ErrorCode.READ_FAIL.getCode();
        String message = ErrorCode.READ_FAIL.getMessage();

        // when
        ErrorResponseDto errorResponseDto = ErrorResponseDto.of(code, message);

        // then
        assertThat(errorResponseDto.getCode()).isEqualTo(code);
        assertThat(errorResponseDto.getMessage()).isEqualTo(message);
    }

    @Test
    @DisplayName("Throwable_로부터_ErrorResponseVo_를_생성한다")
    void createResponseVoFromThrowable() {
        // given
        RuntimeException runtimeException = new RuntimeException("테스트 예외 메시지");

        // when
        ErrorResponseDto errorResponseDto = ErrorResponseDto.of(runtimeException);

        // then
        assertThat(errorResponseDto.getCode()).isEqualTo(ErrorCode.UNKNOWN_FAIL.getCode());
        assertThat(errorResponseDto.getMessage()).isEqualTo("테스트 예외 메시지");
    }

    @Test
    @DisplayName("Throwable_의_메시지가_null_이면_기본_메시지를_사용한다")
    void useDefaultMessageWhenThrowableMessageIsNull() {
        // given
        RuntimeException runtimeException = new RuntimeException((String) null);

        // when
        ErrorResponseDto errorResponseDto = ErrorResponseDto.of(runtimeException);

        // then
        assertThat(errorResponseDto.getCode()).isEqualTo(ErrorCode.UNKNOWN_FAIL.getCode());
        assertThat(errorResponseDto.getMessage()).isEqualTo(ErrorCode.UNKNOWN_FAIL.getMessage());
    }

    @Test
    @DisplayName("Throwable_의_메시지가_빈값이면_기본_메시지를_사용한다")
    void useDefaultMessageWhenThrowableMessageIsEmpty() {
        // given
        RuntimeException runtimeException = new RuntimeException("   ");

        // when
        ErrorResponseDto errorResponseDto = ErrorResponseDto.of(runtimeException);

        // then
        assertThat(errorResponseDto.getMessage()).isEqualTo(ErrorCode.UNKNOWN_FAIL.getMessage());
    }

    @Test
    @DisplayName("Throwable_이_null_이면_기본_메시지를_사용한다")
    void useDefaultMessageWhenThrowableIsNull() {
        // given
        ErrorResponseDto errorResponseDto = ErrorResponseDto.of((Throwable) null);

        // when, then
        assertThat(errorResponseDto.getCode()).isEqualTo(ErrorCode.UNKNOWN_FAIL.getCode());
        assertThat(errorResponseDto.getMessage()).isEqualTo(ErrorCode.UNKNOWN_FAIL.getMessage());
    }
}
