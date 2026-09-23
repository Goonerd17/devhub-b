package teamdevhub.devhub.auth.core.auth.port.facade;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.auth.core.auth.application.service.AuthResult;
import teamdevhub.devhub.auth.core.auth.port.in.command.LoginCommand;
import teamdevhub.devhub.auth.core.auth.port.in.facade.AuthFacade;
import teamdevhub.devhub.shared.core.common.exception.DomainRuleException;
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
        userLoginUseCase.givenWithdrawnUser(TEST_USER_GUID_1);

        LoginCommand loginCommand = new LoginCommand(TEST_EMAIL_1, TEST_PASSWORD_1);

        // when, then
        assertThatThrownBy(() -> authFacade.login(loginCommand))
                .isInstanceOf(DomainRuleException.class);

        assertThat(userLoginUseCase.isLoginTimeUpdated(TEST_USER_GUID_1)).isFalse();
    }
}

