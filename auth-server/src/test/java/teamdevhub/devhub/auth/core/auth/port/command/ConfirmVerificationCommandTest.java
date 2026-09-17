package teamdevhub.devhub.auth.core.auth.port.command;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.auth.core.auth.domain.vo.verification.VerificationTarget;
import teamdevhub.devhub.auth.core.auth.port.in.command.verification.ConfirmVerificationCommand;

import static org.assertj.core.api.Assertions.assertThat;

public class ConfirmVerificationCommandTest {

    private static final VerificationTarget VERIFICATION_TARGET_1 = VerificationTarget.of(
            teamdevhub.devhub.auth.core.auth.domain.vo.verification.VerificationType.EMAIL, "test@test.com");

    @Test
    @DisplayName("빌더로_ConfirmVerificationCommand_를_생성할_수_있다")
    void confirmVerificationCommandBuilderWorks() {
        // given
        VerificationTarget verificationTarget = VERIFICATION_TARGET_1;
        String code = "123456";

        // when
        ConfirmVerificationCommand confirmVerificationCommand = ConfirmVerificationCommand.builder()
                .verificationTarget(verificationTarget)
                .code(code)
                .build();

        // then
        assertThat(confirmVerificationCommand.verificationTarget()).isEqualTo(verificationTarget);
        assertThat(confirmVerificationCommand.code()).isEqualTo(code);
    }
}
