package teamdevhub.devhub.auth.outbound.member;

import org.springframework.stereotype.Component;
import teamdevhub.devhub.auth.core.port.out.AuthMemberPort;
import teamdevhub.devhub.shared.member.AuthMemberRegistration;
import teamdevhub.devhub.shared.security.MemberRole;

@Component
public class AuthMemberHttpClient implements AuthMemberPort {
    private final AuthMemberFeignClient client;

    public AuthMemberHttpClient(AuthMemberFeignClient client) {
        this.client = client;
    }

    public void register(AuthMemberRegistration registration) {
        client.register(registration);
    }
    public boolean adminExists() {
        return client.adminExists();
    }
    public void assertCanLogIn(String memberGuid) {
        client.assertCanLogIn(memberGuid);
    }
    public void recordSuccessfulLogin(String memberGuid) {
        client.recordSuccessfulLogin(memberGuid);
    }
    public MemberRole findCurrentRole(String memberGuid) {
        return client.findCurrentRole(memberGuid);
    }
}
