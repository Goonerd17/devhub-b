package teamdevhub.devhub.query.outbound.skilltrend.persistence;

import java.time.LocalDate;
import java.util.List;

public interface SkillTrendQueryDao {

    long countTotalProjects();

    long countActiveUsers();

    double avgMannerDegree();

    long countNewSkills(LocalDate since);

    List<Object[]> findTopDemandedSkills(int limit);

    List<Object[]> findTopPopularPositions(int limit);

    List<Object[]> findSkillDemandByPosition();

    List<Object[]> findMonthlyTimeline(LocalDate since);

    List<Object[]> findSkillDemandCounts(int limit);

    List<Object[]> findSkillSupplyCounts(int limit);

    List<Object[]> findMarketableSkills();
}
