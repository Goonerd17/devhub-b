package teamdevhub.devhub.query.outbound.skilltrend.http;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Component;
import teamdevhub.devhub.shared.analytics.CodeCount;
import teamdevhub.devhub.shared.analytics.MonthlyProjectCount;
import teamdevhub.devhub.shared.analytics.PositionSkillCount;
import teamdevhub.devhub.query.core.port.out.ProjectAnalyticsPort;

@Component
public class ProjectAnalyticsHttpClient implements ProjectAnalyticsPort {
    private final ProjectAnalyticsFeignClient client;

    public ProjectAnalyticsHttpClient(ProjectAnalyticsFeignClient client) {
        this.client = client;
    }

    @Override
    public long countProjects() {
        return client.countProjects();
    }

    @Override
    public long countNewSkills(LocalDate since) {
        return client.countNewSkills(since);
    }

    @Override
    public List<CodeCount> demandedSkills(int limit) {
        return client.demandedSkills(limit);
    }

    @Override
    public List<CodeCount> popularPositions(int limit) {
        return client.popularPositions(limit);
    }

    @Override
    public List<PositionSkillCount> skillDemandByPosition() {
        return client.skillDemandByPosition();
    }

    @Override
    public List<MonthlyProjectCount> monthlyTimeline(LocalDate since) {
        return client.monthlyTimeline(since);
    }
}
