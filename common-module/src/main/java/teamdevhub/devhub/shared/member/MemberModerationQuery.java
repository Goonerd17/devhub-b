package teamdevhub.devhub.shared.member;

import java.util.List;

public interface MemberModerationQuery {
    List<String> findMemberGuidsByStatus(String statusCode);
    List<MemberModerationProfile> findModerationProfiles(List<String> memberGuids);
}
