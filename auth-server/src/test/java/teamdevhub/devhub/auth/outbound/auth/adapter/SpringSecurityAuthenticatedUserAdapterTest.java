package teamdevhub.devhub.auth.outbound.auth.adapter;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import teamdevhub.devhub.auth.core.auth.domain.vo.user.AuthenticatedUser;
import teamdevhub.devhub.auth.outbound.auth.adapter.SpringSecurityAuthenticatedUserAdapter;
import teamdevhub.devhub.auth.outbound.security.auth.UserAuthentication;
import teamdevhub.devhub.shared.security.MemberRole;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static teamdevhub.devhub.constant.UserTestConstant.*;

public class SpringSecurityAuthenticatedUserAdapterTest {

    @Test
    @DisplayName("?대찓?쇨낵_鍮꾨?踰덊샇濡?AuthenticatedUser_瑜?議고쉶?쒕떎")
    void getAuthenticatedUserByEmailAndPassword() {
        // given
        AuthenticationManager authenticationManager = mock(AuthenticationManager.class);
        SpringSecurityAuthenticatedUserAdapter springSecurityAuthenticatedUserAdapter =
                new SpringSecurityAuthenticatedUserAdapter(authenticationManager);
        AuthenticatedUser authenticatedUser = new AuthenticatedUser(
                TEST_USER_GUID_1,
                TEST_EMAIL_1,
                MemberRole.USER
        );

        Authentication authentication = mock(Authentication.class);
        given(authentication.getPrincipal()).willReturn(new UserAuthentication(authenticatedUser));

        given(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .willReturn(authentication);

        // when
        AuthenticatedUser result = springSecurityAuthenticatedUserAdapter.getAuthenticatedUser(TEST_EMAIL_1, TEST_PASSWORD_1);

        // then
        assertThat(result).isNotNull();
        assertThat(result.userGuid()).isEqualTo(TEST_USER_GUID_1);
        assertThat(result.loginId()).isEqualTo(TEST_EMAIL_1);
        assertThat(result.userRole()).isEqualTo(MemberRole.USER);
    }
}
