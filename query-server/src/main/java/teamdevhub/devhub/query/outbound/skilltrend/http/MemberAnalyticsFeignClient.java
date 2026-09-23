package teamdevhub.devhub.query.outbound.skilltrend.http;

import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import teamdevhub.devhub.query.core.port.out.MemberAnalyticsPort;
import teamdevhub.devhub.shared.analytics.*;
import teamdevhub.devhub.shared.internal.InternalFeignConfiguration;

@FeignClient(name = "member-server", configuration = InternalFeignConfiguration.class)
public interface MemberAnalyticsFeignClient extends MemberAnalyticsPort {
    @Override @GetMapping("/internal/analytics/members/active/count") long countActiveMembers();
    @Override @GetMapping("/internal/analytics/members/manner-degree/average") double averageMannerDegree();
    @Override @GetMapping("/internal/analytics/members/supplied-skills") List<CodeCount> suppliedSkills(@RequestParam int limit);
    @Override @GetMapping("/internal/analytics/members/marketable-skills") List<PositionSkillCount> marketableSkills();
}
