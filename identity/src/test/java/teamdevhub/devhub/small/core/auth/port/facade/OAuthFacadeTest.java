package teamdevhub.devhub.identity.core.auth.port.facade;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.identity.core.auth.application.service.oauth.vo.OAuthResult;
import teamdevhub.devhub.identity.core.auth.application.service.oauth.vo.OAuthAuthorizationResult;
import teamdevhub.devhub.identity.core.auth.application.service.oauth.vo.OAuthUserResult;
import teamdevhub.devhub.identity.core.auth.application.service.oauth.SignupStatus;
import teamdevhub.devhub.identity.core.auth.domain.vo.user.AuthenticatedUser;
import teamdevhub.devhub.identity.core.auth.port.in.facade.OAuthFacade;
import teamdevhub.devhub.member.core.user.domain.User;
import teamdevhub.devhub.member.api.MemberRole;
import teamdevhub.devhub.member.core.user.domain.vo.command.CreateUserCommand;
import teamdevhub.devhub.identity.core.user.port.in.command.SignupUserCommand;
import teamdevhub.devhub.fake.pure.application.port.in.usecase.auth.FakeAuthenticationUseCase;
import teamdevhub.devhub.fake.pure.application.port.in.usecase.auth.oauth.FakeOAuthAuthenticationUseCase;
import teamdevhub.devhub.fake.pure.application.port.in.usecase.auth.oauth.FakeOAuthResolveUseCase;
import teamdevhub.devhub.fake.pure.application.port.in.usecase.user.FakeUserLoginUseCase;

import teamdevhub.devhub.platform.core.common.exception.DomainRuleException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static teamdevhub.devhub.constant.UserTestConstant.*;

public class OAuthFacadeTest {

    private OAuthFacade oauthAuthFacade;

    private FakeOAuthAuthenticationUseCase oauthAuthenticationUseCase;
    private FakeOAuthResolveUseCase oauthResolveUseCase;
    private FakeAuthenticationUseCase authenticationUseCase;
    private FakeUserLoginUseCase userLoginUseCase;

    @BeforeEach
    void init() {
        oauthAuthenticationUseCase = new FakeOAuthAuthenticationUseCase();
        oauthResolveUseCase = new FakeOAuthResolveUseCase();
        authenticationUseCase = new FakeAuthenticationUseCase();
        userLoginUseCase = new FakeUserLoginUseCase();

        oauthAuthFacade = new OAuthFacade(oauthAuthenticationUseCase, oauthResolveUseCase, authenticationUseCase, userLoginUseCase);
    }

    @Test
    @DisplayName("provider_瑜?諛쏆쑝硫?createOAuthAuthorizationUrl_濡?由щ떎?대젆??URL_怨?state_瑜?由ы꽩諛쏆쓣_???덈떎")
    void createOAuthAuthorizationUrlDelegates() {
        // when
        OAuthAuthorizationResult result = oauthAuthFacade.createOAuthAuthorizationUrl("google");

        // then
        assertThat(result.url()).isEqualTo("https://oauth.test/google");
        assertThat(result.state()).isEqualTo("test-state");
    }

    @Test
    @DisplayName("媛?낅맂_?좎?硫?濡쒓렇??泥섎━_??OAuthAuthResponseDto.loggedIn_??諛섑솚?쒕떎")
    void handleOAuthCallbackLoginForCompletedUser() {
        // given
        AuthenticatedUser signupCompletedUser = new AuthenticatedUser(
                TEST_USER_GUID_1,
                TEST_EMAIL_1,
                MemberRole.USER
        );
        oauthResolveUseCase.setOauthUserResult(OAuthUserResult.success(signupCompletedUser));

        // when
        OAuthResult oauthAuthResult = oauthAuthFacade.handleOAuthCallback("google", "code123");

        // then
        assertThat(oauthAuthResult.accessToken()).isNotNull();
        assertThat(oauthAuthResult.signupStatus()).isEqualTo(SignupStatus.COMPLETED);
        assertThat(authenticationUseCase.getLastLoginUser()).isNotNull();
    }

    @Test
    @DisplayName("OAuth_濡쒓렇???깃났_??理쒖쥌_濡쒓렇???쇱떆媛_?낅뜲?댄듃?쒕떎")
    void handleOAuthCallbackUpdatesLastLoginDateTime() {
        // given
        AuthenticatedUser signupCompletedUser = new AuthenticatedUser(
                TEST_USER_GUID_1,
                TEST_EMAIL_1,
                MemberRole.USER
        );
        oauthResolveUseCase.setOauthUserResult(OAuthUserResult.success(signupCompletedUser));

        // when
        oauthAuthFacade.handleOAuthCallback("google", "code123");

        // then
        assertThat(userLoginUseCase.isLoginTimeUpdated(TEST_USER_GUID_1)).isTrue();
    }

    @Test
    @DisplayName("?덊눜???좎?媛_OAuth_濡쒓렇?몄쓣_?쒕룄?섎㈃_?덉쇅媛_諛쒖깮?쒕떎")
    void handleOAuthCallback_withdrawnUser_throwsException() {
        // given
        SignupUserCommand signupUserCommand = SignupUserCommand.builder()
                .email(TEST_EMAIL_1).password(TEST_PASSWORD_1).username(TEST_USERNAME_1)
                .introduction(TEST_INTRO_1).positionList(TEST_POSITION_LIST).skillList(TEST_SKILL_LIST)
                .verificationTarget(VERIFICATION_TARGET_1).build();
        User withdrawnUser = User.createGeneralUser(new CreateUserCommand(TEST_USER_GUID_1, signupUserCommand.username(), signupUserCommand.introduction(), signupUserCommand.positionList(), signupUserCommand.skillList()));
        withdrawnUser.withdraw();
        userLoginUseCase.givenUser(withdrawnUser);

        AuthenticatedUser withdrawnAuthUser = new AuthenticatedUser(TEST_USER_GUID_1, TEST_EMAIL_1, MemberRole.USER);
        oauthResolveUseCase.setOauthUserResult(OAuthUserResult.success(withdrawnAuthUser));

        // when, then
        assertThatThrownBy(() -> oauthAuthFacade.handleOAuthCallback("google", "code789"))
                .isInstanceOf(DomainRuleException.class);
    }

    @Test
    @DisplayName("媛?낅릺吏_?딆?_?좎?硫?濡쒓렇??泥섎━_??OAuthAuthResponseDto.fromCallback_??諛섑솚?쒕떎")
    void handleOAuthCallbackRequiresSignup() {
        // given
        oauthResolveUseCase.setOauthUserResult(OAuthUserResult.requiresSignup());

        // when
        OAuthResult oauthAuthResult = oauthAuthFacade.handleOAuthCallback("google", "code456");

        // then
        assertThat(oauthAuthResult.accessToken()).isNull();
        assertThat(oauthAuthResult.signupStatus()).isEqualTo(SignupStatus.PENDING);
    }
}
