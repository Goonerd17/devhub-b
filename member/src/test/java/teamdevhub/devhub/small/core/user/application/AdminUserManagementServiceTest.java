package teamdevhub.devhub.member.core.user.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.platform.core.common.exception.DomainRuleException;
import teamdevhub.devhub.member.core.user.application.service.AdminUserManagementService;
import teamdevhub.devhub.member.core.user.domain.User;
import teamdevhub.devhub.member.core.user.domain.vo.command.CreateUserCommand;
import teamdevhub.devhub.member.core.user.port.in.command.AdminUpdateUserCommand;
import teamdevhub.devhub.member.core.user.port.in.command.BanUserCommand;
import teamdevhub.devhub.identity.core.user.port.in.command.SignupUserCommand;
import teamdevhub.devhub.fake.pure.application.port.out.user.FakeUserRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static teamdevhub.devhub.constant.UserTestConstant.*;

class AdminUserManagementServiceTest {

    private AdminUserManagementService adminUserManagementService;
    private FakeUserRepository userRepository;

    @BeforeEach
    void init() {
        userRepository = new FakeUserRepository();
        adminUserManagementService = new AdminUserManagementService(userRepository);

        SignupUserCommand signupUserCommand = SignupUserCommand.builder()
                .email(TEST_EMAIL_1).password(TEST_PASSWORD_1).username(TEST_USERNAME_1)
                .introduction(TEST_INTRO_1).positionList(TEST_POSITION_LIST)
                .skillList(TEST_SKILL_LIST).verificationTarget(VERIFICATION_TARGET_1).build();
        User user = User.createGeneralUser(
                new CreateUserCommand(TEST_USER_GUID_1, signupUserCommand.username(), signupUserCommand.introduction(), signupUserCommand.positionList(), signupUserCommand.skillList()));
        userRepository.save(user);
    }

    @Test
    @DisplayName("사용자를_정지하면_blocked_상태가_되고_저장된다")
    void banUser_setsBlockedAndSaves() {
        // given
        BanUserCommand banUserCommand = BanUserCommand.builder()
                .userGuid(TEST_USER_GUID_1)
                .blockEndDate(TEST_BLOCK_END_DATE)
                .build();

        // when
        adminUserManagementService.banUser(banUserCommand);

        // then
        User saved = userRepository.findByUserGuid(TEST_USER_GUID_1);
        assertThat(saved.isBlocked()).isTrue();
        assertThat(saved.getBlockEndDate()).isEqualTo(TEST_BLOCK_END_DATE);
        assertThat(userRepository.wasCalled("save")).isTrue();
    }

    @Test
    @DisplayName("이미_정지된_사용자를_다시_정지하면_예외가_발생한다")
    void banUser_alreadyBanned_throwsException() {
        // given
        BanUserCommand banUserCommand = BanUserCommand.builder()
                .userGuid(TEST_USER_GUID_1)
                .blockEndDate(TEST_BLOCK_END_DATE)
                .build();
        adminUserManagementService.banUser(banUserCommand);

        // when, then
        assertThatThrownBy(() -> adminUserManagementService.banUser(banUserCommand))
                .isInstanceOf(DomainRuleException.class)
                .hasMessageContaining("이미 정지된 회원입니다");
    }

    @Test
    @DisplayName("정지된_사용자를_정지_해제하면_blocked_가_false_가_되고_저장된다")
    void unbanUser_clearsBlockedAndSaves() {
        // given
        BanUserCommand banCommand = BanUserCommand.builder()
                .userGuid(TEST_USER_GUID_1)
                .blockEndDate(TEST_BLOCK_END_DATE)
                .build();
        adminUserManagementService.banUser(banCommand);

        // when
        adminUserManagementService.unbanUser(TEST_USER_GUID_1);

        // then
        User saved = userRepository.findByUserGuid(TEST_USER_GUID_1);
        assertThat(saved.isBlocked()).isFalse();
        assertThat(saved.getBlockEndDate()).isNull();
    }

    @Test
    @DisplayName("정지_중이_아닌_사용자를_정지_해제하면_예외가_발생한다")
    void unbanUser_notBanned_throwsException() {
        // when, then
        assertThatThrownBy(() -> adminUserManagementService.unbanUser(TEST_USER_GUID_1))
                .isInstanceOf(DomainRuleException.class)
                .hasMessageContaining("정지 중이 아닌 회원입니다");
    }

    @Test
    @DisplayName("관리자가_사용자_정보를_수정하면_저장된다")
    void updateUser_savesUpdatedInfo() {
        // given
        AdminUpdateUserCommand adminUpdateUserCommand = AdminUpdateUserCommand.builder()
                .userGuid(TEST_USER_GUID_1)
                .username(NEW_USERNAME)
                .introduction(NEW_INTRO)
                .build();

        // when
        adminUserManagementService.updateUser(adminUpdateUserCommand);

        // then
        User saved = userRepository.findByUserGuid(TEST_USER_GUID_1);
        assertThat(saved.getUsername()).isEqualTo(NEW_USERNAME);
        assertThat(saved.getIntroduction()).isEqualTo(NEW_INTRO);
    }
}
