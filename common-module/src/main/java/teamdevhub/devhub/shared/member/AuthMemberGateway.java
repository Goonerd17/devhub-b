package teamdevhub.devhub.shared.member;

import teamdevhub.devhub.shared.security.MemberRole;

public interface AuthMemberGateway {
    void register(AuthMemberRegistration registration);
    boolean adminExists();
    void assertCanLogIn(String memberGuid);
    void recordSuccessfulLogin(String memberGuid);
    MemberRole findCurrentRole(String memberGuid);
}
