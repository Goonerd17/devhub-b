package teamdevhub.devhub.query.core.skilltrend.port.out;

import teamdevhub.devhub.query.core.skilltrend.domain.vo.CardInfo;
import teamdevhub.devhub.query.core.skilltrend.domain.vo.SkillCount;
import teamdevhub.devhub.query.core.skilltrend.domain.vo.PositionCount;

import java.util.List;

public interface LoadSkillStatisticsPort {

    CardInfo loadCardInfo();

    List<SkillCount> loadDemandedSkills(int limit);

    List<PositionCount> loadPopularPositions(int limit);
}
