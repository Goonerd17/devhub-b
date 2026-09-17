package teamdevhub.devhub.auth.core.auth.port.command;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.auth.core.auth.domain.vo.verification.VerificationTarget;
import teamdevhub.devhub.auth.core.auth.port.in.command.verification.IssueVerificationCommand;

import static org.assertj.core.api.Assertions.assertThat;

public class IssueVerificationCommandTest {

    private static final VerificationTarget VERIFICATION_TARGET_1 = VerificationTarget.of(
            teamdevhub.devhub.auth.core.auth.domain.vo.verification.VerificationType.EMAIL, "test@test.com");

    @Test
    @DisplayName("빌더로_IssueVerificationCommand_를_생성할_수_있다")
    void issueVerificationCommandBuilderWorks() {
        // given
        VerificationTarget verificationTarget = VERIFICATION_TARGET_1;

        // when
        IssueVerificationCommand issueVerificationCommand = IssueVerificationCommand.builder()
                .verificationTarget(verificationTarget)
                .build();

        // then
        assertThat(issueVerificationCommand.verificationTarget()).isEqualTo(verificationTarget);
    }
}
