package teamdevhub.devhub.auth.core.auth.domain.vo.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.auth.core.auth.domain.EmailUserCredential;
import teamdevhub.devhub.shared.security.MemberRole;

import static org.assertj.core.api.Assertions.assertThat;

class EmailAuthenticatedUserTest {

    private static final String TEST_USER_GUID_1 = "USER1a1b2c3d4e5f6g7h8i9j10k11l12";
    private static final String TEST_EMAIL_1 = "user1@example.com";
    private static final String TEST_PASSWORD_1 = "password123!";
    private static final String ADMIN_USER_GUID_1 = "1ADMNa1b2c3d4e5f6g7h8i9j10k11l12";
    private static final String ADMIN_EMAIL_1 = "admin1@example.com";
    private static final String ADMIN_PASSWORD_1 = "adminPassword123!";

    @Test
    @DisplayName("??李????????癒?봄筌앹빖梨????밴쉐??롢늺_??而?몴?揶쏅???揶쏅쉴???")
    void create_emailUserCredential_hasCorrectValues() {
        EmailUserCredential emailUserCredential =EmailUserCredential.of(TEST_USER_GUID_1, TEST_EMAIL_1, TEST_PASSWORD_1, MemberRole.USER);

        assertThat(emailUserCredential.getUserGuid()).isEqualTo(TEST_USER_GUID_1);
        assertThat(emailUserCredential.getEmail()).isEqualTo(TEST_EMAIL_1);
        assertThat(emailUserCredential.getPassword()).isEqualTo(TEST_PASSWORD_1);
        assertThat(emailUserCredential.getUserRole()).isEqualTo(MemberRole.USER);
    }

    @Test
    @DisplayName("?온?귐딆쁽_??釉룡에???李???癒?봄筌앹빖梨????밴쉐??????덈뼄")
    void create_emailUserCredential_withAdminRole() {
        EmailUserCredential emailUserCredential = EmailUserCredential.of(ADMIN_USER_GUID_1, ADMIN_EMAIL_1, ADMIN_PASSWORD_1, MemberRole.ADMIN);

        assertThat(emailUserCredential.getUserRole()).isEqualTo(MemberRole.ADMIN);
        assertThat(emailUserCredential.getEmail()).isEqualTo(ADMIN_EMAIL_1);
    }
}
