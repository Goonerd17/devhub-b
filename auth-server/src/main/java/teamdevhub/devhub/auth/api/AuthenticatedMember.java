package teamdevhub.devhub.auth.api;

import teamdevhub.devhub.shared.security.MemberRole;

public interface AuthenticatedMember {
    String userGuid();
    String loginId();
    MemberRole userRole();
}
