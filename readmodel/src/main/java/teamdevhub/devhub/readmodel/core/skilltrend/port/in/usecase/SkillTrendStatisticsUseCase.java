package teamdevhub.devhub.readmodel.core.skilltrend.port.in.usecase;

import teamdevhub.devhub.readmodel.core.skilltrend.domain.vo.CardInfo;
import teamdevhub.devhub.readmodel.core.skilltrend.domain.vo.PositionCount;
import teamdevhub.devhub.readmodel.core.skilltrend.domain.vo.ProjectTimeline;
import teamdevhub.devhub.readmodel.core.skilltrend.domain.vo.SkillCount;

import java.util.List;

public interface SkillTrendStatisticsUseCase {

    CardInfo getCardInfo();

    List<SkillCount> getDemandedSkills(int limit);

    List<PositionCount> getPopularPositions(int limit);

    List<ProjectTimeline> getProjectTimeline(int months);
}
