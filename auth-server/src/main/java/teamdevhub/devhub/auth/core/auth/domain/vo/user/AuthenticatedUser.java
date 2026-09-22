package teamdevhub.devhub.auth.core.auth.domain.vo.user;

import lombok.Builder;
import teamdevhub.devhub.shared.security.MemberRole;
import teamdevhub.devhub.shared.security.AuditablePrincipal;
import teamdevhub.devhub.shared.security.CurrentUserPrincipal;

@Builder
public record AuthenticatedUser(String userGuid, String loginId, MemberRole userRole)
        implements AuditablePrincipal, CurrentUserPrincipal, teamdevhub.devhub.auth.api.AuthenticatedMember {

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
