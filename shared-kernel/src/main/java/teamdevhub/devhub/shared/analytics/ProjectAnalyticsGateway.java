package teamdevhub.devhub.shared.analytics;

import java.time.LocalDate;
import java.util.List;

public interface ProjectAnalyticsGateway {
    long countProjects();
    long countNewSkills(LocalDate since);
    List<CodeCount> demandedSkills(int limit);
    List<CodeCount> popularPositions(int limit);
    List<PositionSkillCount> skillDemandByPosition();
    List<MonthlyProjectCount> monthlyTimeline(LocalDate since);
}
