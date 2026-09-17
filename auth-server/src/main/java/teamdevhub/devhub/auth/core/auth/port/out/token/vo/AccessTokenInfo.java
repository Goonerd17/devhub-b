package teamdevhub.devhub.auth.core.auth.port.out.token.vo;

import lombok.Builder;
import teamdevhub.devhub.member.api.MemberRole;

@Builder
public record AccessTokenInfo(String userGuid, String email, MemberRole userRole) {}
