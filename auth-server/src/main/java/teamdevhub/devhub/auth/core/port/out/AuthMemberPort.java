package teamdevhub.devhub.auth.core.port.out;

import teamdevhub.devhub.shared.member.AuthMemberRegistration;
import teamdevhub.devhub.shared.security.MemberRole;

public interface AuthMemberPort {
    void register(AuthMemberRegistration registration);
    boolean adminExists();
    void assertCanLogIn(String memberGuid);
    void recordSuccessfulLogin(String memberGuid);
    MemberRole findCurrentRole(String memberGuid);
}
