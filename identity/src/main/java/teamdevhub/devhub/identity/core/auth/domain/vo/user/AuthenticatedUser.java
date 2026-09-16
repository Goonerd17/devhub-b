package teamdevhub.devhub.identity.core.auth.domain.vo.user;

import lombok.Builder;
import teamdevhub.devhub.member.api.MemberRole;
import teamdevhub.devhub.platform.security.AuditablePrincipal;
import teamdevhub.devhub.platform.security.CurrentUserPrincipal;

@Builder
public record AuthenticatedUser(String userGuid, String loginId, MemberRole userRole)
        implements AuditablePrincipal, CurrentUserPrincipal, teamdevhub.devhub.identity.api.AuthenticatedMember {

    @Override
    public String auditIdentifier() {
        return userGuid;
    }

    @Override
    public String roleName() {
        return userRole.name();
    }

    public static AuthenticatedUser of(String userGuid, String loginId, MemberRole userRole) {
        return new AuthenticatedUser(userGuid, loginId, userRole);
    }
}
