package teamdevhub.devhub.query.outbound.skilltrend.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import teamdevhub.devhub.shared.analytics.CodeCount;
import teamdevhub.devhub.shared.analytics.MemberAnalyticsGateway;
import teamdevhub.devhub.shared.analytics.MonthlyProjectCount;
import teamdevhub.devhub.shared.analytics.PositionSkillCount;
import teamdevhub.devhub.shared.analytics.ProjectAnalyticsGateway;

@Repository
@RequiredArgsConstructor
public class SkillTrendQueryDaoImpl implements SkillTrendQueryDao {

    private final ProjectAnalyticsGateway projectAnalyticsGateway;
    private final MemberAnalyticsGateway memberAnalyticsGateway;

    @Override
    public long countTotalProjects() {
        return projectAnalyticsGateway.countProjects();
    }

    @Override
    public long countActiveUsers() {
        return memberAnalyticsGateway.countActiveMembers();
    }

    @Override
    public double avgMannerDegree() {
        return memberAnalyticsGateway.averageMannerDegree();
    }

    @Override
    public long countNewSkills(LocalDate since) {
        return projectAnalyticsGateway.countNewSkills(since);
    }

    @Override
    public List<Object[]> findTopDemandedSkills(int limit) {
        return projectAnalyticsGateway.demandedSkills(limit).stream().map(SkillTrendQueryDaoImpl::row).toList();
    }

    @Override
    public List<Object[]> findTopPopularPositions(int limit) {
        return projectAnalyticsGateway.popularPositions(limit).stream().map(SkillTrendQueryDaoImpl::row).toList();
    }

    @Override
    public List<Object[]> findSkillDemandByPosition() {
        return projectAnalyticsGateway.skillDemandByPosition().stream().map(SkillTrendQueryDaoImpl::row).toList();
    }

    @Override
    public List<Object[]> findMonthlyTimeline(LocalDate since) {
        return projectAnalyticsGateway.monthlyTimeline(since).stream().map(SkillTrendQueryDaoImpl::row).toList();
    }

    @Override
    public List<Object[]> findSkillDemandCounts(int limit) {
        return findTopDemandedSkills(limit);
    }

    @Override
    public List<Object[]> findSkillSupplyCounts(int limit) {
        return memberAnalyticsGateway.suppliedSkills(limit).stream().map(SkillTrendQueryDaoImpl::row).toList();
    }

    @Override
    public List<Object[]> findMarketableSkills() {
        return memberAnalyticsGateway.marketableSkills().stream().map(SkillTrendQueryDaoImpl::row).toList();
    }

    private static Object[] row(CodeCount value) {
        return new Object[] { value.code(), value.count() };
    }

    private static Object[] row(PositionSkillCount value) {
        return new Object[] { value.positionCode(), value.skillCode(), value.count() };
    }

    private static Object[] row(MonthlyProjectCount value) {
        return new Object[] { value.year(), value.month(), value.recruitingCount(), value.progressingCount() };
    }
}
