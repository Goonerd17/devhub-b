package teamdevhub.devhub.query.core.skilltrend.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teamdevhub.devhub.query.core.skilltrend.domain.vo.CardInfo;
import teamdevhub.devhub.query.core.skilltrend.domain.vo.PositionCount;
import teamdevhub.devhub.query.core.skilltrend.domain.vo.ProjectTimeline;
import teamdevhub.devhub.query.core.skilltrend.domain.vo.SkillCount;
import teamdevhub.devhub.query.core.skilltrend.port.in.usecase.SkillTrendStatisticsUseCase;
import teamdevhub.devhub.query.core.skilltrend.port.out.LoadProjectTimelinePort;
import teamdevhub.devhub.query.core.skilltrend.port.out.LoadSkillStatisticsPort;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SkillTrendStatisticsService implements SkillTrendStatisticsUseCase {

    private final LoadSkillStatisticsPort loadSkillStatisticsPort;
    private final LoadProjectTimelinePort loadProjectTimelinePort;

    @Override
    public CardInfo getCardInfo() {
        return loadSkillStatisticsPort.loadCardInfo();
    }

    @Override
    public List<SkillCount> getDemandedSkills(int limit) {
        return loadSkillStatisticsPort.loadDemandedSkills(limit);
    }

    @Override
    public List<PositionCount> getPopularPositions(int limit) {
        return loadSkillStatisticsPort.loadPopularPositions(limit);
    }

    @Override
    public List<ProjectTimeline> getProjectTimeline(int months) {
        return loadProjectTimelinePort.loadMonthlyTimeline(months);
    }
}
