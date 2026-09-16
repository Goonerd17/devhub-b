package teamdevhub.devhub.identity.core.auth.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.identity.core.auth.domain.EmailUserCredential;
import teamdevhub.devhub.identity.core.auth.domain.OAuthUserCredential;
import teamdevhub.devhub.platform.core.common.exception.DomainRuleException;
import teamdevhub.devhub.member.api.MemberRole;
import teamdevhub.devhub.platform.shared.enums.ErrorCode;
import teamdevhub.devhub.identity.core.auth.domain.VerificationProvider;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UserCredentialTest {

    private static final String TEST_USER_GUID_1 = "USER1a1b2c3d4e5f6g7h8i9j10k11l12";
    private static final String TEST_EMAIL_1 = "user1@example.com";
    private static final String TEST_PASSWORD_1 = "password123!";
    private static final String TEST_OAUTH_ID_1 = "testOAuthId";

    @Test
    @DisplayName("??李???????곷????밴쉐??롢늺_筌뤴뫀諭??袁⑤굡揶쎛_??쇱젟??뺣뼄")
    void createEmailCredential_allFieldsSet() {
        // when
        EmailUserCredential emailUserCredential = EmailUserCredential.of(TEST_USER_GUID_1, TEST_EMAIL_1, TEST_PASSWORD_1, MemberRole.USER);

        // then
        assertThat(emailUserCredential.getUserGuid()).isEqualTo(TEST_USER_GUID_1);
        assertThat(emailUserCredential.getEmail()).isEqualTo(TEST_EMAIL_1);
        assertThat(emailUserCredential.getPassword()).isEqualTo(TEST_PASSWORD_1);
        assertThat(emailUserCredential.getUserRole()).isEqualTo(MemberRole.USER);
    }

    @Test
    @DisplayName("??쑬?甕곕뜇?뉐첎?_??깊뒄??롢늺_verifyPassword???紐꾪뀱??猷???됱뇚揶쎛_獄쏆뮇源???_??낅뮉??")
    void verifyPassword_passwordMatches_noException() {
        // given
        EmailUserCredential emailUserCredential = EmailUserCredential.of(TEST_USER_GUID_1, TEST_EMAIL_1, TEST_PASSWORD_1, MemberRole.USER);

        // when, then ??no exception
        emailUserCredential.verifyPassword(true);
    }

    @Test
    @DisplayName("??쑬?甕곕뜇?뉐첎?_??깊뒄???_??놁몵筌?verifyPassword_?紐꾪뀱??USER_PASSWORD_FAIL_??됱뇚揶쎛_獄쏆뮇源??뺣뼄")
    void verifyPassword_passwordNotMatches_throwsDomainRuleException() {
        // given
        EmailUserCredential emailUserCredential = EmailUserCredential.of(TEST_USER_GUID_1, TEST_EMAIL_1, TEST_PASSWORD_1, MemberRole.USER);

        // when, then
        assertThatThrownBy(() -> emailUserCredential.verifyPassword(false))
                .isInstanceOf(DomainRuleException.class)
                .hasMessageContaining(ErrorCode.USER_PASSWORD_FAIL.getMessage());
    }

    @Test
    @DisplayName("changePassword???紐꾪뀱??롢늺_??쑬?甕곕뜇?뉐첎?_??덉쨮??揶쏅??앮에?癰궰野껋럥留??")
    void changePassword_updatesPassword() {
        // given
        EmailUserCredential emailUserCredential = EmailUserCredential.of(TEST_USER_GUID_1, TEST_EMAIL_1, TEST_PASSWORD_1, MemberRole.USER);

        // when
        emailUserCredential.changePassword("newEncodedPassword");

        // then
        assertThat(emailUserCredential.getPassword()).isEqualTo("newEncodedPassword");
    }

    @Test
    @DisplayName("OAuth_?????곷????밴쉐??롢늺_筌뤴뫀諭??袁⑤굡揶쎛_??쇱젟??뺣뼄")
    void createOAuthCredential_allFieldsSet() {
        // when
        OAuthUserCredential credential = OAuthUserCredential.builder()
                .userGuid(TEST_USER_GUID_1)
                .oauthId(TEST_OAUTH_ID_1)
                .verificationProvider(VerificationProvider.GOOGLE)
                .userRole(MemberRole.USER)
                .build();

        // then
        assertThat(credential.getUserGuid()).isEqualTo(TEST_USER_GUID_1);
        assertThat(credential.getOauthId()).isEqualTo(TEST_OAUTH_ID_1);
        assertThat(credential.getVerificationProvider()).isEqualTo(VerificationProvider.GOOGLE);
        assertThat(credential.getUserRole()).isEqualTo(MemberRole.USER);
    }
}
