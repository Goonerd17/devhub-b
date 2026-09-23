package teamdevhub.devhub.query.outbound.skilltrend.http;

import java.time.LocalDate;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import teamdevhub.devhub.query.core.port.out.ProjectAnalyticsPort;
import teamdevhub.devhub.shared.analytics.*;
import teamdevhub.devhub.shared.internal.InternalFeignConfiguration;

@FeignClient(name = "project-server", contextId = "projectAnalyticsClient",
        configuration = InternalFeignConfiguration.class)
public interface ProjectAnalyticsFeignClient extends ProjectAnalyticsPort {
    @Override @GetMapping("/internal/analytics/projects/count") long countProjects();
    @Override @GetMapping("/internal/analytics/projects/new-skills/count") long countNewSkills(@RequestParam LocalDate since);
    @Override @GetMapping("/internal/analytics/projects/demanded-skills") List<CodeCount> demandedSkills(@RequestParam int limit);
    @Override @GetMapping("/internal/analytics/projects/popular-positions") List<CodeCount> popularPositions(@RequestParam int limit);
    @Override @GetMapping("/internal/analytics/projects/skill-demand-by-position") List<PositionSkillCount> skillDemandByPosition();
    @Override @GetMapping("/internal/analytics/projects/monthly-timeline") List<MonthlyProjectCount> monthlyTimeline(@RequestParam LocalDate since);
}
