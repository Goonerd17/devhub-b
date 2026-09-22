package teamdevhub.devhub.shared.member;

import java.util.List;

public interface MemberProfileQuery {
    List<MemberPublicProfile> findPublicProfilesByMemberGuids(List<String> memberGuids);

    List<MemberCommunityProfile> findCommunityProfiles(List<String> memberGuids);
}
