package teamdevhub.devhub.integration;

import org.junit.jupiter.api.Test;
import teamdevhub.devhub.auth.core.auth.port.in.command.oauth.SignupOAuthUserCommand;
import teamdevhub.devhub.auth.core.port.out.AuthMemberPort;
import teamdevhub.devhub.auth.core.user.application.service.UserSignupService;
import teamdevhub.devhub.auth.core.user.port.in.command.SignupAdminCommand;
import teamdevhub.devhub.auth.core.user.port.in.command.SignupUserCommand;
import teamdevhub.devhub.shared.identifier.IdentifierProvider;
import teamdevhub.devhub.shared.member.AuthMemberRegistration;
import teamdevhub.devhub.shared.security.MemberRole;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class AuthMemberRegistrationContractTest {

    private final AuthMemberPort memberPort = mock(AuthMemberPort.class);
    private final IdentifierProvider identifiers = () -> "generated-admin-guid";
    private final UserSignupService service = new UserSignupService(memberPort, identifiers);

    @Test
    void emailSignupUsesMemberRegistrationContract() {
        service.saveEmailUserInfo(new SignupUserCommand("email", "password", "user", "intro",
                List.of("P"), List.of("S"), List.of(), null), "user-guid");

        var registration = registeredValue();
        assertThat(registration.userGuid()).isEqualTo("user-guid");
        assertThat(registration.role()).isEqualTo(MemberRole.USER);
        assertThat(registration.positionList()).containsExactly("P");
    }

    @Test
    void oauthSignupUsesMemberRegistrationContract() {
        service.saveOAuthUserInfo(new SignupOAuthUserCommand("temp", "user", "intro",
                List.of("P"), List.of("S"), List.of()), "oauth-guid");

        var registration = registeredValue();
        assertThat(registration.userGuid()).isEqualTo("oauth-guid");
        assertThat(registration.role()).isEqualTo(MemberRole.USER);
    }

    @Test
    void adminSignupGeneratesGuidThroughIdentifierPort() {
        when(memberPort.adminExists()).thenReturn(false);
        service.initializeAdminUser(new SignupAdminCommand(null, "email", "password", "admin", "intro",
                List.of(), List.of(), null));

        var registration = registeredValue();
        assertThat(registration.userGuid()).isEqualTo("generated-admin-guid");
        assertThat(registration.role()).isEqualTo(MemberRole.ADMIN);
    }

    private AuthMemberRegistration registeredValue() {
        var captor = org.mockito.ArgumentCaptor.forClass(AuthMemberRegistration.class);
        verify(memberPort).register(captor.capture());
        return captor.getValue();
    }
}
