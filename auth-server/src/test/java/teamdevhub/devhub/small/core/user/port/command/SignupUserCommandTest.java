package teamdevhub.devhub.auth.core.user.port.command;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.auth.core.user.port.in.command.SignupUserCommand;
import static teamdevhub.devhub.constant.UserTestConstant.*;

import static org.assertj.core.api.Assertions.assertThat;

class SignupUserCommandTest {


    @Test
    @DisplayName("Builder_로_SignupCommand_를_생성할_수_있다")
    void convertRequestDtoToCommand() {
        // given, when
        SignupUserCommand signupUserCommand = SignupUserCommand.builder()
                .email(TEST_EMAIL_1)
                .password(TEST_PASSWORD_1)
                .username(TEST_USERNAME_1)
                .introduction(TEST_INTRO_1)
                .positionList(TEST_POSITION_LIST)
                .skillList(TEST_SKILL_LIST)
                .verificationTarget(VERIFICATION_TARGET_1)
                .build();

        // then
        assertThat(signupUserCommand.email()).isEqualTo(TEST_EMAIL_1);
        assertThat(signupUserCommand.password()).isEqualTo(TEST_PASSWORD_1);
        assertThat(signupUserCommand.username()).isEqualTo(TEST_USERNAME_1);
        assertThat(signupUserCommand.introduction()).isEqualTo(TEST_INTRO_1);
        assertThat(signupUserCommand.positionList()).containsExactlyElementsOf(TEST_POSITION_LIST);
        assertThat(signupUserCommand.skillList()).containsExactlyElementsOf(TEST_SKILL_LIST);
    }
}
