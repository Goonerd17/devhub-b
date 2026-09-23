package teamdevhub.devhub.project.outbound.member;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import teamdevhub.devhub.shared.internal.InternalFeignConfiguration;
import teamdevhub.devhub.project.core.port.out.ProjectMemberPort;
import teamdevhub.devhub.shared.member.*;

@FeignClient(name = "member-server", configuration = InternalFeignConfiguration.class)
public interface ProjectMemberFeignClient extends ProjectMemberPort {
    @Override @GetMapping("/internal/members/{memberGuid}/project-owner")
    MemberProjectOwner findProjectOwner(@PathVariable String memberGuid);
    @Override @GetMapping("/internal/members/application-profiles")
    Map<String, MemberApplicationProfile> findApplicationProfiles(@RequestParam List<String> memberGuids);
    @GetMapping("/internal/members/{revieweeGuid}/review-score")
    Double findReviewScoreValue(@PathVariable String revieweeGuid, @RequestParam String projectGuid);
    @Override default Optional<Double> findReviewScore(String projectGuid, String revieweeGuid) {
        return Optional.ofNullable(findReviewScoreValue(revieweeGuid, projectGuid));
    }
}
