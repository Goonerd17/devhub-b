package teamdevhub.devhub.medium.outbound.auth.adapter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import teamdevhub.devhub.identity.core.auth.domain.vo.user.AuthenticatedUser;
import teamdevhub.devhub.member.api.MemberRole;
import teamdevhub.devhub.identity.outbound.auth.adapter.UserCredentialAdapter;
import teamdevhub.devhub.identity.outbound.auth.persistence.JpaEmailCredentialRepository;
import teamdevhub.devhub.identity.outbound.auth.persistence.JpaOAuthCredentialRepository;
import teamdevhub.devhub.identity.core.auth.domain.VerificationProvider;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static teamdevhub.devhub.constant.UserTestConstant.*;

@SpringBootTest(classes = teamdevhub.devhub.DevhubApplication.class)
@Transactional
class AuthenticatedUserAdapterTest {

    @Autowired
    private UserCredentialAdapter userCredentialAdapter;

    @Autowired
    private JpaEmailCredentialRepository jpaEmailCredentialRepository;

    @Autowired
    private JpaOAuthCredentialRepository jpaOAuthCredentialRepository;

    @BeforeEach
    void init() {
        jpaEmailCredentialRepository.deleteAll();
        jpaOAuthCredentialRepository.deleteAll();
    }

    @Test
    @DisplayName("??李???癒?봄筌앹빖梨?????館釉?쭖?DB?????貫留??")
    void saveEmailUserCredential_savedToDb() {
        // given
        AuthenticatedUser authenticatedUser = AuthenticatedUser.of(TEST_USER_GUID_1, TEST_EMAIL_1, MemberRole.USER);

        // when
        userCredentialAdapter.saveEmailUserCredential(authenticatedUser, TEST_PASSWORD_1);

        // then
        assertThat(jpaEmailCredentialRepository.findByEmail(TEST_EMAIL_1)).isPresent();
    }

    @Test
    @DisplayName("??李??곗쨮_?癒?봄筌앹빖梨??鈺곌퀬???뺣뼄")
    void findEmailUserCredentialByEmail_found() {
        // given
        AuthenticatedUser authenticatedUser = AuthenticatedUser.of(TEST_USER_GUID_1, TEST_EMAIL_1, MemberRole.USER);
        userCredentialAdapter.saveEmailUserCredential(authenticatedUser, TEST_PASSWORD_1);

        // when
        Optional<AuthenticatedUser> result = userCredentialAdapter.findEmailUserCredentialByEmail(TEST_EMAIL_1);

        // then
        assertThat(result).isPresent();
        assertThat(result.get().loginId()).isEqualTo(TEST_EMAIL_1);
        assertThat(result.get().userGuid()).isEqualTo(TEST_USER_GUID_1);
        assertThat(result.get().userRole()).isEqualTo(MemberRole.USER);
    }

    @Test
    @DisplayName("鈺곕똻????_??낅뮉_??李??곗쨮_鈺곌퀬???롢늺_Optional_empty_??獄쏆꼹???뺣뼄")
    void findEmailUserCredentialByEmail_notFound_returnsEmpty() {
        // when
        Optional<AuthenticatedUser> result = userCredentialAdapter.findEmailUserCredentialByEmail("notexist@email.com");

        // then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("userGuid_嚥???李???癒?봄筌앹빖梨??鈺곌퀬???뺣뼄")
    void findEmailUserCredentialByUserGuid_found() {
        // given
        AuthenticatedUser authenticatedUser = AuthenticatedUser.of(TEST_USER_GUID_1, TEST_EMAIL_1, MemberRole.USER);
        userCredentialAdapter.saveEmailUserCredential(authenticatedUser, TEST_PASSWORD_1);

        // when
        Optional<AuthenticatedUser> result = userCredentialAdapter.findUserCredentialByUserGuid(TEST_USER_GUID_1);

        // then
        assertThat(result).isPresent();
        assertThat(result.get().userGuid()).isEqualTo(TEST_USER_GUID_1);
        assertThat(result.get().loginId()).isEqualTo(TEST_EMAIL_1);
    }

    @Test
    @DisplayName("鈺곕똻????_??낅뮉_userGuid_嚥?鈺곌퀬???롢늺_Optional_empty_??獄쏆꼹???뺣뼄")
    void findEmailUserCredentialByUserGuid_notFound_returnsEmpty() {
        // when
        Optional<AuthenticatedUser> result = userCredentialAdapter.findUserCredentialByUserGuid("NOT_EXIST_GUID");

        // then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("OAuth_?癒?봄筌앹빖梨?????館釉?쭖?DB?????貫留??")
    void saveOAuthUserCredential_savedToDb() {
        // given
        AuthenticatedUser authenticatedUser = AuthenticatedUser.of(TEST_USER_GUID_1, TEST_EMAIL_1, MemberRole.USER);

        // when
        userCredentialAdapter.saveOAuthUserCredential(authenticatedUser, VerificationProvider.GOOGLE, TEST_OAUTH_ID_1);

        // then
        assertThat(jpaOAuthCredentialRepository.findByProviderAndOauthId(VerificationProvider.GOOGLE, TEST_OAUTH_ID_1)).isPresent();
    }

    @Test
    @DisplayName("OAuth_??볥궗?癒?_oauthId_嚥??癒?봄筌앹빖梨??鈺곌퀬???뺣뼄")
    void findOAuthUserCredentialByOAuth_found() {
        // given
        AuthenticatedUser authenticatedUser = AuthenticatedUser.of(TEST_USER_GUID_1, TEST_EMAIL_1, MemberRole.USER);
        userCredentialAdapter.saveOAuthUserCredential(authenticatedUser, VerificationProvider.GOOGLE, TEST_OAUTH_ID_1);

        // when
        Optional<AuthenticatedUser> result = userCredentialAdapter.findOAuthUserCredentialByOAuth(VerificationProvider.GOOGLE, TEST_OAUTH_ID_1);

        // then
        assertThat(result).isPresent();
        assertThat(result.get().userGuid()).isEqualTo(TEST_USER_GUID_1);
    }

    @Test
    @DisplayName("鈺곕똻????_??낅뮉_OAuth_?類ｋ궖嚥?鈺곌퀬???롢늺_Optional_empty_??獄쏆꼹???뺣뼄")
    void findOAuthUserCredentialByOAuth_notFound_returnsEmpty() {
        // when
        Optional<AuthenticatedUser> result = userCredentialAdapter.findOAuthUserCredentialByOAuth(VerificationProvider.GOOGLE, "not-exist-id");

        // then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("??삘뀲_??볥궗?癒?벥_??덉뵬??oauthId_??癰귢쑬猷꾣에????貫留??")
    void saveOAuthUserCredential_differentProviders_storedSeparately() {
        // given
        AuthenticatedUser googleCredential = AuthenticatedUser.of(TEST_USER_GUID_1, TEST_EMAIL_1, MemberRole.USER);
        AuthenticatedUser githubCredential = AuthenticatedUser.of(TEST_USER_GUID_2, TEST_EMAIL_2, MemberRole.USER);

        userCredentialAdapter.saveOAuthUserCredential(googleCredential, VerificationProvider.GOOGLE, TEST_OAUTH_ID_1);
        userCredentialAdapter.saveOAuthUserCredential(githubCredential, VerificationProvider.GITHUB, TEST_OAUTH_ID_1);

        // when
        Optional<AuthenticatedUser> google = userCredentialAdapter.findOAuthUserCredentialByOAuth(VerificationProvider.GOOGLE, TEST_OAUTH_ID_1);
        Optional<AuthenticatedUser> github = userCredentialAdapter.findOAuthUserCredentialByOAuth(VerificationProvider.GITHUB, TEST_OAUTH_ID_1);

        // then
        assertThat(google).isPresent();
        assertThat(github).isPresent();
        assertThat(google.get().userGuid()).isNotEqualTo(github.get().userGuid());
    }
}
