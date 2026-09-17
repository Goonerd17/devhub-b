package teamdevhub.devhub.query.outbound.skilltrend.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import teamdevhub.devhub.query.core.skilltrend.domain.vo.CardInfo;
import teamdevhub.devhub.query.core.skilltrend.domain.vo.PositionCount;
import teamdevhub.devhub.query.core.skilltrend.domain.vo.ProjectTimeline;
import teamdevhub.devhub.query.core.skilltrend.domain.vo.SkillCount;
import teamdevhub.devhub.query.core.skilltrend.port.out.LoadProjectTimelinePort;
import teamdevhub.devhub.query.core.skilltrend.port.out.LoadSkillStatisticsPort;
import teamdevhub.devhub.query.outbound.skilltrend.persistence.SkillTrendQueryDao;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SkillTrendStatisticsAdapter implements LoadSkillStatisticsPort, LoadProjectTimelinePort {

    private final SkillTrendQueryDao skillTrendQueryDao;

    @Override
    public CardInfo loadCardInfo() {
        LocalDate thirtyDaysAgo = LocalDate.now().minusDays(30);
        return CardInfo.of(
                skillTrendQueryDao.countTotalProjects(),
                skillTrendQueryDao.countActiveUsers(),
                skillTrendQueryDao.avgMannerDegree(),
                skillTrendQueryDao.countNewSkills(thirtyDaysAgo)
        );
    }

    @Override
    public List<SkillCount> loadDemandedSkills(int limit) {
        return skillTrendQueryDao.findTopDemandedSkills(limit)
                .stream()
                .map(row -> SkillCount.of((String) row[0], ((Number) row[1]).longValue()))
                .toList();
    }

    @Override
    public List<PositionCount> loadPopularPositions(int limit) {
        return skillTrendQueryDao.findTopPopularPositions(limit)
                .stream()
                .map(row -> PositionCount.of((String) row[0], ((Number) row[1]).longValue()))
                .toList();
    }

    @Override
    public List<ProjectTimeline> loadMonthlyTimeline(int months) {
        LocalDate since = LocalDate.now().minusMonths(months);
        return skillTrendQueryDao.findMonthlyTimeline(since)
                .stream()
                .map(row -> ProjectTimeline.of(
                        ((Number) row[0]).intValue(),
                        ((Number) row[1]).intValue(),
                        row[2] != null ? ((Number) row[2]).longValue() : 0L,
                        row[3] != null ? ((Number) row[3]).longValue() : 0L
                ))
                .toList();
    }
}
