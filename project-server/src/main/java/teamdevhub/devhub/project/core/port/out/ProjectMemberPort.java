package teamdevhub.devhub.project.core.port.out;
import java.util.*;
import teamdevhub.devhub.shared.member.*;
public interface ProjectMemberPort {
    MemberProjectOwner findProjectOwner(String memberGuid);
    Map<String, MemberApplicationProfile> findApplicationProfiles(List<String> memberGuids);
    Optional<Double> findReviewScore(String projectGuid, String revieweeGuid);
}
