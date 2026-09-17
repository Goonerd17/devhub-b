package teamdevhub.devhub.query.core.skilltrend.port.in.usecase;

import teamdevhub.devhub.query.core.skilltrend.domain.vo.CardInfo;
import teamdevhub.devhub.query.core.skilltrend.domain.vo.PositionCount;
import teamdevhub.devhub.query.core.skilltrend.domain.vo.ProjectTimeline;
import teamdevhub.devhub.query.core.skilltrend.domain.vo.SkillCount;

import java.util.List;

public interface SkillTrendStatisticsUseCase {

    CardInfo getCardInfo();

    List<SkillCount> getDemandedSkills(int limit);

    List<PositionCount> getPopularPositions(int limit);

    List<ProjectTimeline> getProjectTimeline(int months);
}
