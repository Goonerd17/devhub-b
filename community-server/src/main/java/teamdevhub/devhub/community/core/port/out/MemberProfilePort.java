package teamdevhub.devhub.community.core.port.out;
import java.util.List; import teamdevhub.devhub.shared.member.*;
public interface MemberProfilePort { List<MemberPublicProfile> findPublicProfilesByMemberGuids(List<String> ids); List<MemberCommunityProfile> findCommunityProfiles(List<String> ids); }
