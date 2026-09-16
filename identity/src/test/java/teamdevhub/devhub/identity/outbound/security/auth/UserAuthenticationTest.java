package teamdevhub.devhub.identity.outbound.security.auth;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import teamdevhub.devhub.identity.outbound.security.auth.UserAuthentication;
import teamdevhub.devhub.identity.core.auth.domain.vo.user.AuthenticatedUser;
import teamdevhub.devhub.member.api.MemberRole;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

public class UserAuthenticationTest {

    private static final String TEST_USER_GUID_1 = "USER1a1b2c3d4e5f6g7h8i9j10k11l12";
    private static final String TEST_EMAIL_1 = "user1@example.com";

    @Test
    @DisplayName("AuthenticatedUser_瑜?Wrapping_?댁꽌_UserDetails_媛_諛섑솚?쒕떎")
    void wrapAuthenticatedUserToUserDetails() {
        // given, when
        AuthenticatedUser user = new AuthenticatedUser(
                TEST_USER_GUID_1,
                TEST_EMAIL_1,
                MemberRole.USER
        );
        UserAuthentication authentication = new UserAuthentication(user);

        // then
        assertThat(authentication.getUser()).isEqualTo(user);
        assertThat(authentication.getUsername()).isEqualTo(TEST_USER_GUID_1);

        Collection<?> authorities = authentication.getAuthorities();
        assertThat(authorities).hasSize(1);
        assertThat(authorities.iterator().next())
                .isEqualTo(new SimpleGrantedAuthority(MemberRole.USER.getAuthority()));

        assertThat(authentication.isAccountNonExpired()).isTrue();
        assertThat(authentication.isAccountNonLocked()).isTrue();
        assertThat(authentication.isCredentialsNonExpired()).isTrue();
        assertThat(authentication.isEnabled()).isTrue();
        assertThat(authentication.getUserGuid()).isEqualTo(TEST_USER_GUID_1);
    }
}
