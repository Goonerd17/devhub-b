package teamdevhub.devhub.medium.outbound.common.persistence.jpa.audit;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import teamdevhub.devhub.identity.core.auth.domain.vo.user.AuthenticatedUser;
import teamdevhub.devhub.platform.outbound.common.persistence.jpa.audit.AuditorAwareProvider;
import teamdevhub.devhub.identity.outbound.security.auth.UserAuthentication;
import teamdevhub.devhub.member.api.MemberRole;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static teamdevhub.devhub.constant.UserTestConstant.*;

class AuditorAwareProviderTest {

    private AuditorAwareProvider auditorAwareProvider;

    @BeforeEach
    void init() {
        auditorAwareProvider = new AuditorAwareProvider();
        SecurityContextHolder.clearContext();
    }

    @AfterEach
    void clear() {
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("?몄쬆_?뺣낫媛_?놁쑝硫?system_??諛섑솚?쒕떎")
    void returnSystemIfNoauthentication() {
        // given
        SecurityContextHolder.clearContext();

        // when
        Optional<String> auditor = auditorAwareProvider.getCurrentAuditor();

        // then
        assertThat(auditor).contains(AuditorAwareProvider.SYSTEM);
    }

    @Test
    @DisplayName("anonymousUser_硫?system_??諛섑솚?쒕떎")
    void returnSystemIfAnonymousUser() {
        // given
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                "anonymousUser",
                null,
                Collections.emptyList()
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // when
        Optional<String> auditor = auditorAwareProvider.getCurrentAuditor();

        // then
        assertThat(auditor).contains(AuditorAwareProvider.SYSTEM);
    }

    @Test
    @DisplayName("UserAuthentication_?대㈃_USERGUID_瑜?諛섑솚?쒕떎")
    void returnUserEmailIfUserAuthentication() {
        // given
        AuthenticatedUser authenticatedUser = new AuthenticatedUser(
                TEST_USER_GUID_1,
                TEST_EMAIL_1,
                MemberRole.USER
        );

        UserAuthentication userAuthentication = new UserAuthentication(authenticatedUser);

        Authentication authentication = new UsernamePasswordAuthenticationToken(userAuthentication, null, Collections.emptyList());

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // when
        Optional<String> auditor = auditorAwareProvider.getCurrentAuditor();

        // then
        assertThat(auditor).contains(TEST_USER_GUID_1);
    }

    @Test
    @DisplayName("principal_??AuthenticatedUser_硫?USERGUID_瑜?諛섑솚?쒕떎")
    void returnEmailIfPrincipalIsAuthenticatedUser() {
        // given
        AuthenticatedUser authenticatedUser = new AuthenticatedUser(
                TEST_USER_GUID_1,
                TEST_EMAIL_1,
                MemberRole.USER
        );

        Authentication authentication = new UsernamePasswordAuthenticationToken(authenticatedUser, null, Collections.emptyList());

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // when
        Optional<String> auditor = auditorAwareProvider.getCurrentAuditor();

        // then
        assertThat(auditor).contains(TEST_USER_GUID_1);
    }

    @Test
    @DisplayName("?????녿뒗_principal_?대㈃_system_??諛섑솚?쒕떎")
    void returnSystemIfPrincipalUnknown() {
        // given
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                new Object(),
                null,
                Collections.emptyList()
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // when
        Optional<String> auditor = auditorAwareProvider.getCurrentAuditor();

        // then
        assertThat(auditor).contains(AuditorAwareProvider.SYSTEM);
    }
}