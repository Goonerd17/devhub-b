package teamdevhub.devhub.auth.core.auth.domain.vo.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.auth.core.auth.domain.OAuthUserCredential;
import teamdevhub.devhub.member.api.MemberRole;
import teamdevhub.devhub.auth.core.auth.domain.VerificationProvider;

import static org.assertj.core.api.Assertions.assertThat;

class OAuthAuthenticatedUserTest {

    private static final String TEST_OAUTH_ID_1 = "testOAuthId";
    private static final String TEST_USER_GUID_1 = "USER1a1b2c3d4e5f6g7h8i9j10k11l12";

    @Test
    @DisplayName("OAuth_??????癒?봄筌앹빖梨????밴쉐??롢늺_??而?몴?揶쏅???揶쏅쉴???")
    void create_oauthUserCredential_hasCorrectValues() {
        OAuthUserCredential oauthUserCredential = new OAuthUserCredential(
                TEST_USER_GUID_1, TEST_OAUTH_ID_1, VerificationProvider.GOOGLE, MemberRole.USER
        );

        assertThat(oauthUserCredential.getUserGuid()).isEqualTo(TEST_USER_GUID_1);
        assertThat(oauthUserCredential.getVerificationProvider()).isEqualTo(VerificationProvider.GOOGLE);
        assertThat(oauthUserCredential.getOauthId()).isEqualTo(TEST_OAUTH_ID_1);
        assertThat(oauthUserCredential.getUserRole()).isEqualTo(MemberRole.USER);
    }

    @Test
    @DisplayName("??쇰펶??OAuth_??볥궗?癒?쨮_?癒?봄筌앹빖梨????밴쉐??????덈뼄")
    void create_oauthUserCredential_withDifferentProviders() {
        for (VerificationProvider verificationProvider : new VerificationProvider[]{
                VerificationProvider.GOOGLE, VerificationProvider.GITHUB,
                VerificationProvider.KAKAO, VerificationProvider.NAVER}) {

            OAuthUserCredential oauthUserCredential = new OAuthUserCredential(
                    TEST_USER_GUID_1, TEST_OAUTH_ID_1, verificationProvider, MemberRole.USER
            );

            assertThat(oauthUserCredential.getVerificationProvider()).isEqualTo(verificationProvider);
        }
    }
}
