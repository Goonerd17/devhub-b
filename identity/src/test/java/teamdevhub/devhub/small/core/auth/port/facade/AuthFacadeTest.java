package teamdevhub.devhub.identity.core.auth.port.facade;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.identity.core.auth.application.service.AuthResult;
import teamdevhub.devhub.identity.core.auth.port.in.command.LoginCommand;
import teamdevhub.devhub.identity.core.auth.port.in.facade.AuthFacade;
import teamdevhub.devhub.platform.core.common.exception.DomainRuleException;
import teamdevhub.devhub.member.core.user.domain.User;
import teamdevhub.devhub.member.core.user.domain.vo.command.CreateUserCommand;
import teamdevhub.devhub.identity.core.user.port.in.command.SignupUserCommand;
import teamdevhub.devhub.fake.pure.application.port.in.usecase.auth.FakeAuthenticationUseCase;
import teamdevhub.devhub.fake.pure.application.port.in.usecase.auth.FakeUserCredentialUseCase;
import teamdevhub.devhub.fake.pure.application.port.in.usecase.user.FakeUserLoginUseCase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static teamdevhub.devhub.constant.UserTestConstant.*;

public class AuthFacadeTest {

    private AuthFacade authFacade;

    private FakeUserCredentialUseCase userCredentialUseCase;
    private FakeAuthenticationUseCase authenticationUseCase;
    private FakeUserLoginUseCase userLoginUseCase;

    @BeforeEach
    void init() {
        userCredentialUseCase = new FakeUserCredentialUseCase();
        authenticationUseCase = new FakeAuthenticationUseCase();
        userLoginUseCase = new FakeUserLoginUseCase();

        authFacade = new AuthFacade(
                userCredentialUseCase,
                authenticationUseCase,
                userLoginUseCase
        );
    }

    private User buildUser(String userGuid) {
        SignupUserCommand signupUserCommand = SignupUserCommand.builder()
                .email(TEST_EMAIL_1).password(TEST_PASSWORD_1).username(TEST_USERNAME_1)
                .introduction(TEST_INTRO_1).positionList(TEST_POSITION_LIST).skillList(TEST_SKILL_LIST)
                .verificationTarget(VERIFICATION_TARGET_1).build();
        return User.createGeneralUser(new CreateUserCommand(userGuid, signupUserCommand.username(), signupUserCommand.introduction(), signupUserCommand.positionList(), signupUserCommand.skillList()));
    }

    @Test
    @DisplayName("loginCommand_濡?濡쒓렇???????덈떎")
    void loginWithLoginCommand() {
        // given
        LoginCommand loginCommand = new LoginCommand(TEST_EMAIL_1, TEST_PASSWORD_1);

        // when
        AuthResult authResult = authFacade.login(loginCommand);

        // then
        assertThat(authResult.accessToken()).isEqualTo("access-token");
    }

    @Test
    @DisplayName("reissueAccessToken_???덈줈??accessToken_??諛섑솚?쒕떎")
    void reissueAccessToken_ReturnsNewAccessToken() {
        // given
        String oldToken = "old-token";

        // when
        AuthResult authResult = authFacade.reissueAccessToken(oldToken);

        // then
        assertThat(authResult.accessToken()).isEqualTo("new-access-token");
    }

    @Test
    @DisplayName("logout_?_revoke_瑜??몄텧?쒕떎")
    void logoutRevokesToken() {
        // given, when
        authFacade.logout(TEST_USER_GUID_1);

        // then
        assertThat(authenticationUseCase.getRevokedUserGuid()).isEqualTo(TEST_USER_GUID_1);
    }

    @Test
    @DisplayName("?대찓??濡쒓렇???깃났_??理쒖쥌_濡쒓렇???쇱떆媛_?낅뜲?댄듃?쒕떎")
    void login_updatesLastLoginDateTime() {
        // given
        LoginCommand loginCommand = new LoginCommand(TEST_EMAIL_1, TEST_PASSWORD_1);

        // when
        authFacade.login(loginCommand);

        // then
        assertThat(userLoginUseCase.isLoginTimeUpdated(TEST_USER_GUID_1)).isTrue();
    }

    @Test
    @DisplayName("?좏겙_?щ컻湲됱?_理쒖쥌_濡쒓렇???쇱떆瑜??낅뜲?댄듃?섏?_?딅뒗??")
    void reissueAccessToken_doesNotUpdateLastLoginDateTime() {
        // given
        String oldToken = "old-token";

        // when
        authFacade.reissueAccessToken(oldToken);

        // then
        assertThat(userLoginUseCase.isLoginTimeUpdated(TEST_USER_GUID_1)).isFalse();
    }

    @Test
    @DisplayName("?덊눜???좎?媛_濡쒓렇?몄쓣_?쒕룄?섎㈃_理쒖쥌_濡쒓렇???쇱떆媛_?낅뜲?댄듃?섏?_?딅뒗??")
    void login_withdrawnUser_doesNotUpdateLastLoginDateTime() {
        // given
        User withdrawnUser = buildUser(TEST_USER_GUID_1);
        withdrawnUser.withdraw();
        userLoginUseCase.givenUser(withdrawnUser);

        LoginCommand loginCommand = new LoginCommand(TEST_EMAIL_1, TEST_PASSWORD_1);

        // when, then
        assertThatThrownBy(() -> authFacade.login(loginCommand))
                .isInstanceOf(DomainRuleException.class);

        assertThat(userLoginUseCase.isLoginTimeUpdated(TEST_USER_GUID_1)).isFalse();
    }
}

