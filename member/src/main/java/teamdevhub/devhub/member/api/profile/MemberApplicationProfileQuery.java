package teamdevhub.devhub.member.api.profile;

import java.util.List;
import java.util.Map;

public interface MemberApplicationProfileQuery {
    Map<String, MemberApplicationProfile> findApplicationProfiles(List<String> memberGuids);
}
