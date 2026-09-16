package teamdevhub.devhub.identity.core.auth.application.service.oauth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.identity.core.auth.application.service.oauth.OAuthResolveService;
import teamdevhub.devhub.identity.core.auth.application.service.oauth.vo.OAuthUserResult;
import teamdevhub.devhub.identity.core.auth.domain.vo.user.AuthenticatedUser;
import teamdevhub.devhub.identity.core.auth.port.in.command.oauth.SignupOAuthUserCommand;
import teamdevhub.devhub.member.api.MemberRole;
import teamdevhub.devhub.fake.pure.application.port.out.auth.FakeUserCredentialRepository;
import teamdevhub.devhub.fake.pure.application.provider.FakeTokenParseProvider;
import teamdevhub.devhub.identity.core.auth.domain.vo.oauth.OAuthUser;
import teamdevhub.devhub.identity.core.auth.port.out.token.vo.TempTokenInfo;
import teamdevhub.devhub.identity.core.auth.domain.VerificationProvider;

import static org.assertj.core.api.Assertions.assertThat;
import static teamdevhub.devhub.constant.UserTestConstant.*;

class OAuthResolveServiceTest {

    private OAuthResolveService oauthResolveService;

    private FakeTokenParseProvider tokenParseProvider;
    private FakeUserCredentialRepository userCredentialRepository;

    @BeforeEach
    void init() {
        tokenParseProvider = new FakeTokenParseProvider();
        userCredentialRepository = new FakeUserCredentialRepository();

        oauthResolveService = new OAuthResolveService(tokenParseProvider, userCredentialRepository);
    }

    @Test
    @DisplayName("OAuth_?醫?揶쎛_鈺곕똻???롢늺_loginAvailable_??true_????")
    void findOrRequireSignup_existingUser_loginAvailableIsTrue() {
        // given
        AuthenticatedUser existingCredential = AuthenticatedUser.of(TEST_USER_GUID_1, TEST_EMAIL_1, MemberRole.USER);
        userCredentialRepository.saveOAuthUserCredential(existingCredential, VerificationProvider.GOOGLE, TEST_OAUTH_ID_1);

        OAuthUser oauthUser = new OAuthUser(TEST_OAUTH_ID_1, VerificationProvider.GOOGLE, TEST_EMAIL_1);

        // when
        OAuthUserResult result = oauthResolveService.findOrRequireSignup(oauthUser);

        // then
        assertThat(result.loginAvailable()).isTrue();
        assertThat(result.authenticatedUser()).isNotNull();
        assertThat(result.authenticatedUser().userGuid()).isEqualTo(TEST_USER_GUID_1);
    }

    @Test
    @DisplayName("OAuth_?醫?揶쎛_鈺곕똻????_??놁몵筌?loginAvailable_??false_????")
    void findOrRequireSignup_nonExistingUser_loginAvailableIsFalse() {
        // given
        OAuthUser oauthUser = new OAuthUser(TEST_OAUTH_ID_1, VerificationProvider.GOOGLE, TEST_EMAIL_1);

        // when
        OAuthUserResult result = oauthResolveService.findOrRequireSignup(oauthUser);

        // then
        assertThat(result.loginAvailable()).isFalse();
        assertThat(result.authenticatedUser()).isNull();
    }

    @Test
    @DisplayName("??삘뀲_??볥궗?癒?벥_??덉뵬??oauthId_??癰귢쑬猷?????癒?쨮_筌ｌ꼶???뺣뼄")
    void findOrRequireSignup_sameOAuthIdDifferentProvider_treatedSeparately() {
        // given
        AuthenticatedUser authenticatedUser = AuthenticatedUser.of(TEST_USER_GUID_1, TEST_EMAIL_1, MemberRole.USER);
        userCredentialRepository.saveOAuthUserCredential(authenticatedUser, VerificationProvider.GOOGLE, TEST_OAUTH_ID_1);

        OAuthUser githubUser = new OAuthUser(TEST_OAUTH_ID_1, VerificationProvider.GITHUB, TEST_EMAIL_1);

        // when
        OAuthUserResult result = oauthResolveService.findOrRequireSignup(githubUser);

        // then
        assertThat(result.loginAvailable()).isFalse();
    }

    @Test
    @DisplayName("tempToken_??곗쨮_OAuthUser_???곕뗄???뺣뼄")
    void extractOAuthUser_validTempToken_returnsOAuthUser() {
        // given
        TempTokenInfo tokenInfo = new TempTokenInfo(TEST_OAUTH_ID_1, VerificationProvider.GOOGLE, TEST_EMAIL_1);
        tokenParseProvider.givenTempToken(TEMP_TOKEN, tokenInfo);

        SignupOAuthUserCommand signupOAuthUserCommand = SignupOAuthUserCommand.builder()
                .tempToken(TEMP_TOKEN)
                .username(TEST_USERNAME_1)
                .introduction(TEST_INTRO_1)
                .positionList(TEST_POSITION_LIST)
                .skillList(TEST_SKILL_LIST)
                .build();

        // when
        OAuthUser oauthUser = oauthResolveService.extractOAuthUser(signupOAuthUserCommand);

        // then
        assertThat(oauthUser.oauthId()).isEqualTo(TEST_OAUTH_ID_1);
        assertThat(oauthUser.verificationProvider()).isEqualTo(VerificationProvider.GOOGLE);
        assertThat(oauthUser.email()).isEqualTo(TEST_EMAIL_1);
    }

    @Test
    @DisplayName("?醫륁뒞???_???_tempToken_??곗쨮_OAuthUser_?곕뗄??????됱뇚揶쎛_獄쏆뮇源??뺣뼄")
    void extractOAuthUser_invalidTempToken_throwsException() {
        // given
        SignupOAuthUserCommand signupOAuthUserCommand = SignupOAuthUserCommand.builder()
                .tempToken("invalid-temp-token")
                .username(TEST_USERNAME_1)
                .introduction(TEST_INTRO_1)
                .positionList(TEST_POSITION_LIST)
                .skillList(TEST_SKILL_LIST)
                .build();

        // when, then
        org.assertj.core.api.Assertions.assertThatThrownBy(
                () -> oauthResolveService.extractOAuthUser(signupOAuthUserCommand))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
