package teamdevhub.devhub.auth.api;

import teamdevhub.devhub.member.api.MemberRole;

public interface AuthenticatedMember {
    String userGuid();
    String loginId();
    MemberRole userRole();
}
