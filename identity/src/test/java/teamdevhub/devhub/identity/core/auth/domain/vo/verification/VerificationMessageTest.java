package teamdevhub.devhub.identity.core.auth.domain.vo.verification;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.identity.core.auth.domain.vo.verification.VerificationMessage;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class VerificationMessageTest {

    private static final String TEST_EMAIL_CODE = "123456";

    @Test
    @DisplayName("빌더를_이용해_객체를_생성할_수_있다")
    void createWithBuilder() {
        // given
        String code = TEST_EMAIL_CODE;
        LocalDateTime expiredAt = LocalDateTime.now().plusMinutes(5);

        // when
        VerificationMessage verificationMessage = VerificationMessage.builder()
                .code(code)
                .expiredAt(expiredAt)
                .build();

        // then
        assertThat(verificationMessage.code()).isEqualTo(code);
        assertThat(verificationMessage.expiredAt()).isEqualTo(expiredAt);
    }

    @Test
    @DisplayName("VerificationMessage.of() 정적 팩토리 메서드로 객체를 생성할 수 있다")
    void createWithOf() {
        // given
        String code = TEST_EMAIL_CODE;
        LocalDateTime expiredAt = LocalDateTime.now().plusMinutes(10);

        // when
        VerificationMessage verificationMessage = VerificationMessage.of(code, expiredAt);

        // then
        assertThat(verificationMessage.code()).isEqualTo(code);
        assertThat(verificationMessage.expiredAt()).isEqualTo(expiredAt);
    }
}
