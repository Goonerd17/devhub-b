package teamdevhub.devhub.readmodel.core.skilltrend.port.in.usecase;

import teamdevhub.devhub.readmodel.core.skilltrend.domain.vo.MarketableSkill;
import teamdevhub.devhub.readmodel.core.skilltrend.domain.vo.SkillDemandByPosition;
import teamdevhub.devhub.readmodel.core.skilltrend.domain.vo.SkillSupplyDemand;

import java.util.List;

public interface SkillTrendAnalyticsUseCase {

    List<SkillDemandByPosition> getSkillDemandByPosition(int limitPerPosition);

    List<SkillSupplyDemand> getSkillSupplyDemand(int limit);

    List<MarketableSkill> getMarketableSkills(int limitPerPosition);
}
