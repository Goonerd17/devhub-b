package teamdevhub.devhub.project.outbound.member;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Component;
import teamdevhub.devhub.shared.member.MemberApplicationProfile;
import teamdevhub.devhub.shared.member.MemberProjectOwner;
import teamdevhub.devhub.project.core.port.out.ProjectMemberPort;

@Component
public class ProjectMemberHttpClient implements ProjectMemberPort {
    private final ProjectMemberFeignClient client;

    public ProjectMemberHttpClient(ProjectMemberFeignClient client) {
        this.client = client;
    }

    @Override
    public MemberProjectOwner findProjectOwner(String memberGuid) {
        return client.findProjectOwner(memberGuid);
    }

    @Override
    public Map<String, MemberApplicationProfile> findApplicationProfiles(List<String> memberGuids) {
        return memberGuids.isEmpty() ? Map.of() : client.findApplicationProfiles(memberGuids);
    }

    @Override
    public Optional<Double> findReviewScore(String projectGuid, String revieweeGuid) {
        return client.findReviewScore(projectGuid, revieweeGuid);
    }
}
