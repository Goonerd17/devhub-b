package teamdevhub.devhub.web.core.user.port.facade;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.identity.core.auth.application.service.oauth.vo.OAuthResult;
import teamdevhub.devhub.identity.core.auth.application.service.oauth.SignupStatus;
import teamdevhub.devhub.identity.core.auth.port.in.command.oauth.SignupOAuthUserCommand;
import teamdevhub.devhub.identity.core.user.port.in.command.SignupUserCommand;
import teamdevhub.devhub.web.core.user.port.in.facade.UserSignupFacade;
import teamdevhub.devhub.fake.pure.application.port.in.usecase.auth.FakeAuthenticationUseCase;
import teamdevhub.devhub.fake.pure.application.port.in.usecase.auth.FakeUserCredentialUseCase;
import teamdevhub.devhub.fake.pure.application.port.in.usecase.auth.oauth.FakeOAuthResolveUseCase;
import teamdevhub.devhub.fake.pure.application.port.in.usecase.auth.verification.FakeVerificationUseCase;
import teamdevhub.devhub.fake.pure.application.port.in.usecase.terms.FakeTermsAgreeUseCase;
import teamdevhub.devhub.identity.core.auth.application.service.AuthResult;
import teamdevhub.devhub.fake.pure.application.port.in.usecase.user.FakeUserLoginUseCase;
import teamdevhub.devhub.fake.pure.application.port.in.usecase.user.FakeUserSignupUseCase;

import static org.assertj.core.api.Assertions.assertThat;
import static teamdevhub.devhub.constant.UserTestConstant.*;

public class UserSignupFacadeTest {

    private UserSignupFacade userSignupFacade;

    private FakeUserSignupUseCase userSignupUseCase;
    private FakeTermsAgreeUseCase termsAgreeUseCase;
    private FakeOAuthResolveUseCase oauthResolveUseCase;
    private FakeUserCredentialUseCase userCredentialUseCase;
    private FakeAuthenticationUseCase authenticationUseCase;
    private FakeVerificationUseCase verificationUseCase;
    private FakeUserLoginUseCase userLoginUseCase;

    @BeforeEach
    void init() {
        userSignupUseCase = new FakeUserSignupUseCase();
        oauthResolveUseCase = new FakeOAuthResolveUseCase();
        termsAgreeUseCase = new FakeTermsAgreeUseCase();
        userCredentialUseCase = new FakeUserCredentialUseCase();
        authenticationUseCase = new FakeAuthenticationUseCase();
        verificationUseCase = new FakeVerificationUseCase();
        userLoginUseCase = new FakeUserLoginUseCase();

        userSignupFacade = new UserSignupFacade(
                userSignupUseCase,
                termsAgreeUseCase,
                oauthResolveUseCase,
                userCredentialUseCase,
                authenticationUseCase,
                verificationUseCase,
                userLoginUseCase
        );
    }

    @Test
    @DisplayName("signup_는_사용자_정보를_정상적으로_반환한다")
    void signupReturnsUser() {
        // given
        SignupUserCommand signupUserCommand = new SignupUserCommand(
                TEST_EMAIL_1,
                TEST_PASSWORD_1,
                TEST_USERNAME_1,
                TEST_INTRO_1,
                TEST_POSITION_LIST,
                TEST_SKILL_LIST,
                TEST_TERMS_AGREEMENT_LIST,
                VERIFICATION_TARGET_1
        );

        // when
        AuthResult authResult = userSignupFacade.signup(signupUserCommand);

        // then
        assertThat(userSignupUseCase.isSignupCalled()).isTrue();
        assertThat(authResult.accessToken()).isEqualTo("access-token");
        assertThat(authResult.refreshToken()).isEqualTo("refresh-token");
    }

    @Test
    @DisplayName("signupWithOAuth_는_signup_후_oauth-access-token_을_포함한_로그인_성공으로_이어진다")
    void signupWithOAuthCallsSignupThenLogin() {
        // given
        SignupOAuthUserCommand signupCommand = new SignupOAuthUserCommand(TEMP_TOKEN, TEST_USERNAME_1, TEST_INTRO_1, TEST_POSITION_LIST, TEST_SKILL_LIST, TEST_TERMS_AGREEMENT_LIST);

        // when
        OAuthResult oauthResult = userSignupFacade.signupWithOAuth(signupCommand);

        // then
        assertThat(oauthResult.accessToken()).isEqualTo("access-token");
        assertThat(oauthResult.signupStatus()).isEqualTo(SignupStatus.COMPLETED);
        assertThat(oauthResult.tempToken()).isNull();
    }
}
