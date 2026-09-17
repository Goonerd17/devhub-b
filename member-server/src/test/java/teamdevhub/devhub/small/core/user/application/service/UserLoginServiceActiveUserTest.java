package teamdevhub.devhub.member.core.user.application.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.member.core.user.application.service.UserLoginService;
import teamdevhub.devhub.member.core.user.domain.User;
import teamdevhub.devhub.member.core.user.domain.vo.command.CreateUserCommand;
import teamdevhub.devhub.auth.core.user.port.in.command.SignupUserCommand;
import teamdevhub.devhub.fake.pure.application.port.out.user.FakeUserRepository;
import teamdevhub.devhub.fake.pure.application.provider.FakeTimeProvider;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThatCode;
import static teamdevhub.devhub.constant.UserTestConstant.*;

class UserLoginServiceActiveUserTest {

    private UserLoginService userLoginService;
    private FakeUserRepository userRepository;
    private FakeTimeProvider timeProvider;

    @BeforeEach
    void init() {
        timeProvider = new FakeTimeProvider(LocalDateTime.of(2025, 6, 1, 9, 0));
        userRepository = new FakeUserRepository();
        userLoginService = new UserLoginService(timeProvider, userRepository);

        SignupUserCommand signupUserCommand = SignupUserCommand.builder()
                .email(TEST_EMAIL_1).password(TEST_PASSWORD_1).username(TEST_USERNAME_1)
                .introduction(TEST_INTRO_1).positionList(TEST_POSITION_LIST)
                .skillList(TEST_SKILL_LIST).verificationTarget(VERIFICATION_TARGET_1).build();
        User user = User.createGeneralUser(
                new CreateUserCommand(TEST_USER_GUID_1, signupUserCommand.username(), signupUserCommand.introduction(), signupUserCommand.positionList(), signupUserCommand.skillList()));
        userRepository.save(user);
    }

    @Test
    @DisplayName("?쒖꽦_?곹깭???ъ슜?먭?_濡쒓렇?몄쓣_?쒕룄?섎㈃_?덉쇅媛_諛쒖깮?섏?_?딅뒗??")
    void validateLoginUser_activeUser_noException() {
        // when, then
        assertThatCode(() -> userLoginService.validateLoginUser(TEST_USER_GUID_1))
                .doesNotThrowAnyException();
    }
}
