package teamdevhub.devhub.query.core.port.out;
import java.time.LocalDate; import java.util.List;
import teamdevhub.devhub.shared.analytics.*;
public interface ProjectAnalyticsPort { long countProjects(); long countNewSkills(LocalDate since); List<CodeCount> demandedSkills(int limit); List<CodeCount> popularPositions(int limit); List<PositionSkillCount> skillDemandByPosition(); List<MonthlyProjectCount> monthlyTimeline(LocalDate since); }
