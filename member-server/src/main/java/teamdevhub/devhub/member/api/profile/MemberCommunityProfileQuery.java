package teamdevhub.devhub.member.api.profile;

import java.util.List;

public interface MemberCommunityProfileQuery {
    List<MemberCommunityProfile> findCommunityProfiles(List<String> memberGuids);
}
