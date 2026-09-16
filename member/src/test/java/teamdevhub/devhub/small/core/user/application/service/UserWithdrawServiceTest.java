package teamdevhub.devhub.member.core.user.application.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.member.core.user.application.service.UserWithdrawService;
import teamdevhub.devhub.member.core.user.domain.User;
import teamdevhub.devhub.member.core.user.domain.vo.command.CreateUserCommand;
import teamdevhub.devhub.fake.pure.application.port.out.user.FakeUserRepository;
import teamdevhub.devhub.identity.core.user.port.in.command.SignupUserCommand;

import static org.assertj.core.api.Assertions.assertThat;
import static teamdevhub.devhub.constant.UserTestConstant.*;

public class UserWithdrawServiceTest {

    private UserWithdrawService userWithdrawService;

    private FakeUserRepository userRepository;

    @BeforeEach
    void init() {
        userRepository = new FakeUserRepository();

        userWithdrawService = new UserWithdrawService(userRepository);
    }

    @Test
    @DisplayName("회원탈퇴한_사용자의_deleted_값은_true_이고_blocked_값은_false_이다")
    void setDeletedTrueWhenUserWithdraws() {
        // given
        SignupUserCommand signupUserCommand = SignupUserCommand.builder()
                .email(TEST_EMAIL_1)
                .password(TEST_PASSWORD_1)
                .username(TEST_USERNAME_1)
                .introduction(TEST_INTRO_1)
                .positionList(TEST_POSITION_LIST)
                .skillList(TEST_SKILL_LIST)
                .verificationTarget(VERIFICATION_TARGET_1)
                .build();
        CreateUserCommand generalCreateUserCommand = new CreateUserCommand(TEST_USER_GUID_1, signupUserCommand.username(), signupUserCommand.introduction(), signupUserCommand.positionList(), signupUserCommand.skillList());
        User testUser = User.createGeneralUser(generalCreateUserCommand);

        userRepository.save(testUser);

        // when
        userWithdrawService.withdraw(testUser.getUserGuid());

        // then
        assertThat(userRepository.findByUserGuid(testUser.getUserGuid()).isDeleted()).isTrue();
        assertThat(userRepository.findByUserGuid(testUser.getUserGuid()).isBlocked()).isFalse();
    }
}
