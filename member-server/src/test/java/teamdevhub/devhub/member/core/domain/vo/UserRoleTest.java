package teamdevhub.devhub.member.core.domain.vo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.member.core.user.domain.vo.UserRole;

import static org.assertj.core.api.Assertions.assertThat;

class UserRoleTest {

    @Test
    @DisplayName("ADMIN_역할은_ROLE_ADMIN_권한을_가진다")
    void isAdminRoleHasRoleAdminAuthority() {
        // given, when
        UserRole userRole = UserRole.ADMIN;

        // then
        assertThat(userRole.getAuthority()).isEqualTo("ROLE_ADMIN");
    }

    @Test
    @DisplayName("USER_역할은_ROLE_USER_권한을_가진다")
    void isUserRoleHasRoleUserAuthority() {
        // given, when
        UserRole userRole = UserRole.USER;


        // then
        assertThat(userRole.getAuthority()).isEqualTo("ROLE_USER");
    }

    @Test
    @DisplayName("getAuthority_상수_값이_UserRole_과_일치한다")
    void getAuthorityMatchesUserRole() {
        // given, when
        assertThat(UserRole.ADMIN.getAuthority())
                // then
                .isEqualTo(UserRole.Authority.ADMIN);

        // given, when
        assertThat(UserRole.USER.getAuthority())
                // then
                .isEqualTo(UserRole.Authority.USER);
    }
}
