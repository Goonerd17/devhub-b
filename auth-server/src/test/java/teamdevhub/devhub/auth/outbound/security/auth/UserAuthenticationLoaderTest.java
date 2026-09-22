package teamdevhub.devhub.auth.outbound.security.auth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import teamdevhub.devhub.auth.core.auth.domain.EmailUserCredential;
import teamdevhub.devhub.auth.core.auth.domain.vo.user.AuthenticatedUser;
import teamdevhub.devhub.auth.core.auth.port.out.EmailUserCredentialRepository;
import teamdevhub.devhub.shared.security.MemberRole;
import teamdevhub.devhub.auth.outbound.security.auth.UserAuthentication;
import teamdevhub.devhub.auth.outbound.security.auth.UserAuthenticationLoader;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserAuthenticationLoaderTest {

    private static final String TEST_EMAIL_1 = "user1@example.com";
    private static final String TEST_PASSWORD_1 = "password123!";
    private static final String TEST_USER_GUID_1 = "USER1a1b2c3d4e5f6g7h8i9j10k11l12";

    private UserAuthenticationLoader userAuthenticationLoader;

    private EmailUserCredentialRepository emailUserCredentialRepository;

    @BeforeEach
    public void init() {
        emailUserCredentialRepository = mock(EmailUserCredentialRepository.class);
        userAuthenticationLoader = new UserAuthenticationLoader(emailUserCredentialRepository);
    }

    @Test
    @DisplayName("鈺곕똻???롫뮉_??李??깆뵠筌?UserAuthentication_??獄쏆꼹???뺣뼄")
    void returnUserAuthenticationIfEmailExists() {
        // given
        EmailUserCredential emailUserCredential = EmailUserCredential.of(
                TEST_USER_GUID_1,
                TEST_EMAIL_1,
                TEST_PASSWORD_1,
                MemberRole.USER
        );

        when(emailUserCredentialRepository.findByEmail(TEST_EMAIL_1))
                .thenReturn(Optional.of(emailUserCredential));

        // when
        UserDetails details = userAuthenticationLoader.loadUserByUsername(TEST_EMAIL_1);

        // then
        assertThat(details).isInstanceOf(UserAuthentication.class);

        UserAuthentication userAuthentication = (UserAuthentication) details;

        AuthenticatedUser expected = AuthenticatedUser.of(
                TEST_USER_GUID_1,
                TEST_EMAIL_1,
                MemberRole.USER
        );

        assertThat(userAuthentication.getUser()).isEqualTo(expected);
        assertThat(userAuthentication.getUsername()).isEqualTo(TEST_USER_GUID_1);
    }

    @Test
    @DisplayName("鈺곕똻????_??낅뮉_??李??깆뵠筌?UsernameNotFoundException_獄쏆뮇源?")
    void throwExceptionIfEmailNotExists() {
        // given
        when(emailUserCredentialRepository.findByEmail("notfound@example.com"))
                .thenReturn(Optional.empty());

        // when & then
        assertThrows(
                UsernameNotFoundException.class,
                () -> userAuthenticationLoader.loadUserByUsername("notfound@example.com")
        );
    }
}
