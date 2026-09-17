package teamdevhub.devhub.auth.core.auth.application.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.auth.core.auth.application.service.UserCredentialService;
import teamdevhub.devhub.auth.core.auth.application.service.token.RefreshToken;
import teamdevhub.devhub.auth.core.auth.domain.vo.oauth.OAuthUser;
import teamdevhub.devhub.auth.core.auth.domain.vo.user.AuthenticatedUser;
import teamdevhub.devhub.auth.core.auth.port.in.command.LoginCommand;
import teamdevhub.devhub.shared.core.common.exception.BusinessRuleException;
import teamdevhub.devhub.auth.api.credential.EmailCredentialRegistrationCommand;
import teamdevhub.devhub.fake.pure.application.port.out.auth.FakeAuthenticatedUserResolver;
import teamdevhub.devhub.fake.pure.application.port.out.auth.FakeRefreshTokenRepository;
import teamdevhub.devhub.fake.pure.application.port.out.auth.FakeUserCredentialRepository;
import teamdevhub.devhub.fake.pure.application.provider.FakeEncodedPasswordProvider;
import teamdevhub.devhub.fake.pure.application.provider.FakeTokenParseProvider;
import teamdevhub.devhub.fake.pure.application.provider.FakeUuidIdentifierProvider;
import teamdevhub.devhub.shared.shared.enums.ErrorCode;
import teamdevhub.devhub.auth.core.auth.domain.VerificationProvider;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static teamdevhub.devhub.constant.UserTestConstant.*;

class UserCredentialServiceTest {

    private UserCredentialService userCredentialService;
    private FakeUserCredentialRepository userCredentialRepository;
    private FakeRefreshTokenRepository refreshTokenRepository;
    private FakeTokenParseProvider tokenParseProvider;

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

    private EmailCredentialRegistrationCommand signupCommand() {
        return new EmailCredentialRegistrationCommand(
                TEST_EMAIL_1,
                TEST_PASSWORD_1
        );
    }

    @Test
    @DisplayName("?대찓???뚯썝媛?????ъ슜??GUID媛_諛섑솚?쒕떎")
    void signupEmailUser_newEmail_returnsUserGuid() {
        // given
        EmailCredentialRegistrationCommand signupUserCommand = signupCommand();

        // when
        String userGuid = userCredentialService.signupEmailUser(signupUserCommand);

        // then
        assertThat(userGuid).isEqualTo(TEST_USER_GUID_1);
        assertThat(userCredentialRepository.findEmailUserCredentialByEmail(TEST_EMAIL_1)).isPresent();
    }

    @Test
    @DisplayName("?대?_媛?낅맂_?대찓?쇰줈_媛?낇븯硫??덉쇅媛_諛쒖깮?쒕떎")
    void signupEmailUser_duplicateEmail_throwsException() {
        // given
        userCredentialService.signupEmailUser(signupCommand());

        // when, then
        assertThatThrownBy(() -> userCredentialService.signupEmailUser(signupCommand()))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining(ErrorCode.DUPLICATED_ACCOUNT.getMessage());
    }

    @Test
    @DisplayName("OAuth_?뚯썝媛????AuthenticatedUser媛_諛섑솚?쒕떎")
    void signupOAuthUser_newOAuthId_returnsAuthenticatedUser() {
        // given
        OAuthUser oauthUser = OAuthUser.builder()
                .oauthId(TEST_OAUTH_ID_1)
                .verificationProvider(VerificationProvider.GOOGLE)
                .email(TEST_EMAIL_1)
                .build();

        // when
        AuthenticatedUser result = userCredentialService.signupOAuthUser(oauthUser);

        // then
        assertThat(result.userGuid()).isEqualTo(TEST_USER_GUID_1);
        assertThat(userCredentialRepository.findOAuthUserCredentialByOAuth(VerificationProvider.GOOGLE, TEST_OAUTH_ID_1)).isPresent();
    }

    @Test
    @DisplayName("?대?_媛?낅맂_OAuth_ID濡?媛?낇븯硫??덉쇅媛_諛쒖깮?쒕떎")
    void signupOAuthUser_duplicateOAuthId_throwsException() {
        // given
        OAuthUser oauthUser = OAuthUser.builder()
                .oauthId(TEST_OAUTH_ID_1)
                .verificationProvider(VerificationProvider.GOOGLE)
                .email(TEST_EMAIL_1)
                .build();
        userCredentialService.signupOAuthUser(oauthUser);

        // when, then
        assertThatThrownBy(() -> userCredentialService.signupOAuthUser(oauthUser))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining(ErrorCode.DUPLICATED_ACCOUNT.getMessage());
    }

    @Test
    @DisplayName("?좏슚??由ы봽?덉떆_?좏겙?쇰줈_?щ컻湲??붿껌?섎㈃_AuthenticatedUser媛_諛섑솚?쒕떎")
    void getUserForReissue_validToken_returnsAuthenticatedUser() {
        // given
        userCredentialService.signupEmailUser(signupCommand());
        RefreshToken savedToken = new RefreshToken(TEST_USER_GUID_1, REFRESH_TOKEN);
        refreshTokenRepository.save(savedToken);
        tokenParseProvider.givenRefreshToken(REFRESH_TOKEN, TEST_USER_GUID_1);

        // when
        AuthenticatedUser result = userCredentialService.getUserForReissue(REFRESH_TOKEN);

        // then
        assertThat(result.userGuid()).isEqualTo(TEST_USER_GUID_1);
    }

    @Test
    @DisplayName("??λ릺吏_?딆?_由ы봽?덉떆_?좏겙?쇰줈_?щ컻湲??붿껌?섎㈃_?덉쇅媛_諛쒖깮?쒕떎")
    void getUserForReissue_tokenNotSaved_throwsException() {
        // given
        tokenParseProvider.givenRefreshToken(REFRESH_TOKEN, TEST_USER_GUID_1);

        // when, then
        assertThatThrownBy(() -> userCredentialService.getUserForReissue(REFRESH_TOKEN))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining(ErrorCode.REFRESH_TOKEN_INVALID.getMessage());
    }

    @Test
    @DisplayName("??λ맂_?좏겙怨??ㅻⅨ_?좏겙?쇰줈_?щ컻湲??붿껌?섎㈃_?덉쇅媛_諛쒖깮?쒕떎")
    void getUserForReissue_tokenMismatch_throwsException() {
        // given
        RefreshToken savedToken = new RefreshToken(TEST_USER_GUID_1, REFRESH_TOKEN);
        refreshTokenRepository.save(savedToken);
        tokenParseProvider.givenRefreshToken("different-token", TEST_USER_GUID_1);

        // when, then
        assertThatThrownBy(() -> userCredentialService.getUserForReissue("different-token"))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining(ErrorCode.REFRESH_TOKEN_INVALID.getMessage());
    }

    @Test
    @DisplayName("濡쒓렇???붿껌?섎㈃_?몄쬆???ъ슜???뺣낫媛_諛섑솚?쒕떎")
    void authenticate_validCredentials_returnsAuthenticatedUser() {
        // given
        LoginCommand loginCommand = LoginCommand.builder()
                .email(TEST_EMAIL_1)
                .password(TEST_PASSWORD_1)
                .build();

        // when
        AuthenticatedUser result = userCredentialService.authenticate(loginCommand);

        // then
        assertThat(result.userGuid()).isEqualTo(TEST_USER_GUID_1);
        assertThat(result.loginId()).isEqualTo(TEST_EMAIL_1);
    }

    @Test
    @DisplayName("?먭꺽利앸챸????λ맂_???대떦_?ъ슜?먯쓽_userGuid濡??щ컻湲됱씠_媛?ν븯??")
    void getUserForReissue_afterSignup_userCredentialExists() {
        // given
        userCredentialService.signupEmailUser(signupCommand());
        RefreshToken savedToken = new RefreshToken(TEST_USER_GUID_1, REFRESH_TOKEN);
        refreshTokenRepository.save(savedToken);
        tokenParseProvider.givenRefreshToken(REFRESH_TOKEN, TEST_USER_GUID_1);

        // when
        AuthenticatedUser result = userCredentialService.getUserForReissue(REFRESH_TOKEN);

        // then
        assertThat(result).isNotNull();
        assertThat(result.userGuid()).isEqualTo(TEST_USER_GUID_1);
    }
}
