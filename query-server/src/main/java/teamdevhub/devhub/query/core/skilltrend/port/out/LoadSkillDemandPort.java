package teamdevhub.devhub.query.core.skilltrend.port.out;

import teamdevhub.devhub.query.core.skilltrend.domain.vo.MarketableSkill;
import teamdevhub.devhub.query.core.skilltrend.domain.vo.SkillDemandByPosition;
import teamdevhub.devhub.query.core.skilltrend.domain.vo.SkillSupplyDemand;

import java.util.List;

public interface LoadSkillDemandPort {

    List<SkillDemandByPosition> loadSkillDemandByPosition(int limitPerPosition);

    List<SkillSupplyDemand> loadSkillSupplyDemand(int limit);

    List<MarketableSkill> loadMarketableSkills(int limitPerPosition);
}
