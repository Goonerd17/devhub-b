package teamdevhub.devhub.readmodel.core.skilltrend.port.out;

import teamdevhub.devhub.readmodel.core.skilltrend.domain.vo.CardInfo;
import teamdevhub.devhub.readmodel.core.skilltrend.domain.vo.SkillCount;
import teamdevhub.devhub.readmodel.core.skilltrend.domain.vo.PositionCount;

import java.util.List;

public interface LoadSkillStatisticsPort {

    CardInfo loadCardInfo();

    List<SkillCount> loadDemandedSkills(int limit);

    List<PositionCount> loadPopularPositions(int limit);
}
