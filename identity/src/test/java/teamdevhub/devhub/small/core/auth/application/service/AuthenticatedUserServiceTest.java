package teamdevhub.devhub.identity.core.auth.application.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.identity.core.auth.application.service.UserCredentialService;
import teamdevhub.devhub.identity.core.auth.application.service.token.RefreshToken;
import teamdevhub.devhub.identity.core.auth.domain.vo.oauth.OAuthUser;
import teamdevhub.devhub.identity.core.auth.domain.vo.user.AuthenticatedUser;
import teamdevhub.devhub.identity.core.auth.port.in.command.LoginCommand;
import teamdevhub.devhub.platform.core.common.exception.BusinessRuleException;
import teamdevhub.devhub.identity.api.credential.EmailCredentialRegistrationCommand;
import teamdevhub.devhub.fake.pure.application.port.out.auth.FakeRefreshTokenRepository;
import teamdevhub.devhub.fake.pure.application.port.out.auth.FakeUserCredentialRepository;
import teamdevhub.devhub.fake.pure.application.provider.FakeAuthenticatedUserResolver;
import teamdevhub.devhub.fake.pure.application.provider.FakeEncodedPasswordProvider;
import teamdevhub.devhub.fake.pure.application.provider.FakeTokenParseProvider;
import teamdevhub.devhub.fake.pure.application.provider.FakeUuidIdentifierProvider;
import teamdevhub.devhub.platform.shared.enums.ErrorCode;
import teamdevhub.devhub.identity.core.auth.domain.VerificationProvider;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static teamdevhub.devhub.constant.UserTestConstant.*;

class AuthenticatedUserServiceTest {

    private UserCredentialService userCredentialService;

    private FakeTokenParseProvider tokenParseProvider;
    private FakeUserCredentialRepository userCredentialRepository;
    private FakeRefreshTokenRepository refreshTokenRepository;

    @BeforeEach
    void init() {
        tokenParseProvider = new FakeTokenParseProvider();
        userCredentialRepository = new FakeUserCredentialRepository();
        refreshTokenRepository = new FakeRefreshTokenRepository();

        userCredentialService = new UserCredentialService(
                tokenParseProvider,
                new FakeUuidIdentifierProvider(TEST_USER_GUID_1),
                new FakeEncodedPasswordProvider(),
                new FakeAuthenticatedUserResolver(),
                userCredentialRepository,
                refreshTokenRepository
        );
    }

    @Test
    @DisplayName("?대찓???뚯썝媛?낆뿉_?깃났?섎㈃_userGuid_瑜?諛섑솚?쒕떎")
    void signupEmailUser_success_returns_userGuid() {
        // given
        EmailCredentialRegistrationCommand signupUserCommand = emailCredentialCommand();

        // when
        String userGuid = userCredentialService.signupEmailUser(signupUserCommand);

        // then
        assertThat(userGuid).isEqualTo(TEST_USER_GUID_1);
        assertThat(userCredentialRepository.findEmailUserCredentialByEmail(TEST_EMAIL_1)).isPresent();
    }

    @Test
    @DisplayName("?대찓???뚯썝媛?????먭꺽利앸챸????λ맂??")
    void signupEmailUser_credentialIsSaved() {
        // given
        EmailCredentialRegistrationCommand signupUserCommand = emailCredentialCommand();

        // when
        userCredentialService.signupEmailUser(signupUserCommand);

        // then
        AuthenticatedUser saved = userCredentialRepository.findEmailUserCredentialByEmail(TEST_EMAIL_1).orElseThrow();
        assertThat(saved.loginId()).isEqualTo(TEST_EMAIL_1);
        assertThat(saved.userGuid()).isEqualTo(TEST_USER_GUID_1);
    }

    @Test
    @DisplayName("以묐났???대찓?쇰줈_?뚯썝媛?????덉쇅媛_諛쒖깮?쒕떎")
    void signupEmailUser_duplicateEmail_throwsException() {
        // given
        EmailCredentialRegistrationCommand signupUserCommand = emailCredentialCommand();

        userCredentialService.signupEmailUser(signupUserCommand);

        // when, then
        assertThatThrownBy(() -> userCredentialService.signupEmailUser(signupUserCommand))
                .isInstanceOf(BusinessRuleException.class);
    }

    @Test
    @DisplayName("OAuth_?뚯썝媛?낆뿉_?깃났?섎㈃_UserCredential_??諛섑솚?쒕떎")
    void signupOAuthUser_success_returns_userCredential() {
        // given
        OAuthUser oauthUser = new OAuthUser(TEST_OAUTH_ID_1, VerificationProvider.GOOGLE, TEST_EMAIL_1);

        // when
        AuthenticatedUser result = userCredentialService.signupOAuthUser(oauthUser);

        // then
        assertThat(result).isNotNull();
        assertThat(result.userGuid()).isEqualTo(TEST_USER_GUID_1);
        assertThat(result.loginId()).isEqualTo(TEST_OAUTH_ID_1);
    }

    @Test
    @DisplayName("以묐났??OAuth_?뺣낫濡??뚯썝媛?????덉쇅媛_諛쒖깮?쒕떎")
    void signupOAuthUser_duplicate_throwsException() {
        // given
        OAuthUser oauthUser = new OAuthUser(TEST_OAUTH_ID_1, VerificationProvider.GOOGLE, TEST_EMAIL_1);
        userCredentialService.signupOAuthUser(oauthUser);

        // when, then
        assertThatThrownBy(() -> userCredentialService.signupOAuthUser(oauthUser))
                .isInstanceOf(BusinessRuleException.class);
    }

    @Test
    @DisplayName("?좏슚??由ы봽?덉떆_?좏겙?쇰줈_UserCredential_??議고쉶?쒕떎")
    void getUserForReissue_validToken_returnsUserCredential() {
        // given
        EmailCredentialRegistrationCommand signupUserCommand = emailCredentialCommand();
        userCredentialService.signupEmailUser(signupUserCommand);

        RefreshToken refreshToken = new RefreshToken(TEST_USER_GUID_1, REFRESH_TOKEN);
        tokenParseProvider.givenRefreshToken(REFRESH_TOKEN, TEST_USER_GUID_1);
        refreshTokenRepository.givenRefreshToken(refreshToken);

        // when
        AuthenticatedUser result = userCredentialService.getUserForReissue(REFRESH_TOKEN);

        // then
        assertThat(result.userGuid()).isEqualTo(TEST_USER_GUID_1);
        assertThat(result.loginId()).isEqualTo(TEST_EMAIL_1);
    }

    @Test
    @DisplayName("??λ릺吏_?딆?_由ы봽?덉떆_?좏겙?쇰줈_?щ컻湲??붿껌?섎㈃_?덉쇅媛_諛쒖깮?쒕떎")
    void getUserForReissue_tokenNotSaved_throwsException() {
        // given: ?좏겙 ?뚯떛? ?섏?留?DB????λ맂 ?좏겙???녿뒗 ?곹솴
        String invalidToken = "invalid-refresh-token";
        tokenParseProvider.givenRefreshToken(invalidToken, TEST_USER_GUID_1);

        // when, then
        assertThatThrownBy(() -> userCredentialService.getUserForReissue(invalidToken))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining(ErrorCode.REFRESH_TOKEN_INVALID.getMessage());
    }

    @Test
    @DisplayName("??λ맂_?좏겙怨??붿껌_?좏겙??遺덉씪移섑븯硫??덉쇅媛_諛쒖깮?쒕떎")
    void getUserForReissue_tokenMismatch_throwsException() {
        // given: ?ㅻⅨ ?좏겙????λ맂 ?곹솴
        String storedToken = "stored-refresh-token";
        String requestToken = "different-refresh-token";

        tokenParseProvider.givenRefreshToken(requestToken, TEST_USER_GUID_1);
        refreshTokenRepository.givenRefreshToken(new RefreshToken(TEST_USER_GUID_1, storedToken));

        // when, then
        assertThatThrownBy(() -> userCredentialService.getUserForReissue(requestToken))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining(ErrorCode.REFRESH_TOKEN_INVALID.getMessage());
    }

    @Test
    @DisplayName("?대찓?쇨낵_鍮꾨?踰덊샇濡??몄쬆?섎㈃_UserCredential_??諛섑솚?쒕떎")
    void authenticate_success_returnsUserCredential() {
        // given
        LoginCommand loginCommand = LoginCommand.builder()
                .email(TEST_EMAIL_1)
                .password(TEST_PASSWORD_1)
                .build();

        // when
        AuthenticatedUser result = userCredentialService.authenticate(loginCommand);

        // then
        assertThat(result).isNotNull();
        assertThat(result.loginId()).isEqualTo(TEST_EMAIL_1);
        assertThat(result.userGuid()).isEqualTo(TEST_USER_GUID_1);
    }

    private EmailCredentialRegistrationCommand emailCredentialCommand() {
        return new EmailCredentialRegistrationCommand(TEST_EMAIL_1, TEST_PASSWORD_1);
    }
}
