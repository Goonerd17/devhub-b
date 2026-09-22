package teamdevhub.devhub.shared.member;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ProjectMemberQuery {
    MemberProjectOwner findProjectOwner(String memberGuid);

    Map<String, MemberApplicationProfile> findApplicationProfiles(List<String> memberGuids);

    Optional<Double> findReviewScore(String projectGuid, String revieweeGuid);
}
