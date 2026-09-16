package teamdevhub.devhub.member.api.profile;

import java.util.List;

public interface MemberPublicProfileQuery {

    List<MemberPublicProfile> findPublicProfilesByMemberGuids(List<String> memberGuids);
}
