package teamdevhub.devhub.shared.member;

import java.util.List;
import teamdevhub.devhub.shared.security.MemberRole;

public record AuthMemberRegistration(String userGuid, String username, String introduction,
        List<String> positionList, List<String> skillList, MemberRole role) {
}
