package teamdevhub.devhub.medium.outbound.auth.adapter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import teamdevhub.devhub.identity.core.auth.domain.EmailUserCredential;
import teamdevhub.devhub.member.api.MemberRole;
import teamdevhub.devhub.identity.outbound.auth.adapter.EmailUserCredentialAdapter;
import teamdevhub.devhub.identity.outbound.auth.adapter.entity.EmailCredentialEntity;
import teamdevhub.devhub.identity.outbound.auth.persistence.JpaEmailCredentialRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static teamdevhub.devhub.constant.UserTestConstant.*;

@SpringBootTest(classes = teamdevhub.devhub.DevhubApplication.class)
@Transactional
class EmailAuthenticatedUserAdapterTest {

    @Autowired
    private EmailUserCredentialAdapter emailUserCredentialAdapter;

    @Autowired
    private JpaEmailCredentialRepository jpaEmailCredentialRepository;

    @BeforeEach
    void init() {
        jpaEmailCredentialRepository.deleteAll();
    }

    @Test
    @DisplayName("?대찓?쇰줈_EmailUserCredential_??議고쉶?쒕떎")
    void findByEmail_found() {
        // given
        jpaEmailCredentialRepository.save(
                EmailCredentialEntity.builder()
                        .userGuid(TEST_USER_GUID_1)
                        .email(TEST_EMAIL_1)
                        .password(TEST_PASSWORD_1)
                        .userRole(MemberRole.USER)
                        .build()
        );

        // when
        Optional<EmailUserCredential> result = emailUserCredentialAdapter.findByEmail(TEST_EMAIL_1);

        // then
        assertThat(result).isPresent();
        assertThat(result.get().getEmail()).isEqualTo(TEST_EMAIL_1);
        assertThat(result.get().getUserGuid()).isEqualTo(TEST_USER_GUID_1);
        assertThat(result.get().getUserRole()).isEqualTo(MemberRole.USER);
    }

    @Test
    @DisplayName("議댁옱?섏?_?딅뒗_?대찓?쇰줈_議고쉶?섎㈃_Optional_empty_瑜?諛섑솚?쒕떎")
    void findByEmail_notFound_returnsEmpty() {
        // when
        Optional<EmailUserCredential> result = emailUserCredentialAdapter.findByEmail("notexist@email.com");

        // then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("userGuid_濡?EmailUserCredential_??議고쉶?쒕떎")
    void findByUserGuid_found() {
        // given
        jpaEmailCredentialRepository.save(
                EmailCredentialEntity.builder()
                        .userGuid(TEST_USER_GUID_1)
                        .email(TEST_EMAIL_1)
                        .password(TEST_PASSWORD_1)
                        .userRole(MemberRole.USER)
                        .build()
        );

        // when
        Optional<EmailUserCredential> result = emailUserCredentialAdapter.findByUserGuid(TEST_USER_GUID_1);

        // then
        assertThat(result).isPresent();
        assertThat(result.get().getUserGuid()).isEqualTo(TEST_USER_GUID_1);
        assertThat(result.get().getEmail()).isEqualTo(TEST_EMAIL_1);
        assertThat(result.get().getPassword()).isEqualTo(TEST_PASSWORD_1);
    }

    @Test
    @DisplayName("議댁옱?섏?_?딅뒗_userGuid_濡?議고쉶?섎㈃_Optional_empty_瑜?諛섑솚?쒕떎")
    void findByUserGuid_notFound_returnsEmpty() {
        // when
        Optional<EmailUserCredential> result = emailUserCredentialAdapter.findByUserGuid("NOT_EXIST_GUID");

        // then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("愿由ъ옄_??븷??EmailUserCredential_???щ컮瑜닿쾶_議고쉶?쒕떎")
    void findByEmail_adminRole_returnsCorrectRole() {
        // given
        jpaEmailCredentialRepository.save(
                EmailCredentialEntity.builder()
                        .userGuid(ADMIN_USER_GUID_1)
                        .email(ADMIN_EMAIL_1)
                        .password(ADMIN_PASSWORD_1)
                        .userRole(MemberRole.ADMIN)
                        .build()
        );

        // when
        Optional<EmailUserCredential> result = emailUserCredentialAdapter.findByEmail(ADMIN_EMAIL_1);

        // then
        assertThat(result).isPresent();
        assertThat(result.get().getUserRole()).isEqualTo(MemberRole.ADMIN);
        assertThat(result.get().getUserGuid()).isEqualTo(ADMIN_USER_GUID_1);
    }
}
