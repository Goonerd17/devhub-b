package teamdevhub.devhub.fake.pure.application.port.in.usecase.skilltrend;

import teamdevhub.devhub.readmodel.core.skilltrend.domain.vo.MarketableSkill;
import teamdevhub.devhub.readmodel.core.skilltrend.domain.vo.SkillDemandByPosition;
import teamdevhub.devhub.readmodel.core.skilltrend.domain.vo.SkillSupplyDemand;
import teamdevhub.devhub.readmodel.core.skilltrend.port.in.usecase.SkillTrendAnalyticsUseCase;

import java.util.ArrayList;
import java.util.List;

public class FakeSkillTrendAnalyticsUseCase implements SkillTrendAnalyticsUseCase {

    private List<SkillDemandByPosition> skillDemandByPosition = new ArrayList<>();
    private List<SkillSupplyDemand> skillSupplyDemand = new ArrayList<>();
    private List<MarketableSkill> marketableSkills = new ArrayList<>();

    public void givenSkillDemandByPosition(List<SkillDemandByPosition> data) {
        this.skillDemandByPosition = new ArrayList<>(data);
    }

    public void givenSkillSupplyDemand(List<SkillSupplyDemand> data) {
        this.skillSupplyDemand = new ArrayList<>(data);
    }

    public void givenMarketableSkills(List<MarketableSkill> data) {
        this.marketableSkills = new ArrayList<>(data);
    }

    @Override
    public List<SkillDemandByPosition> getSkillDemandByPosition(int limitPerPosition) {
        return List.copyOf(skillDemandByPosition);
    }

    @Override
    public List<SkillSupplyDemand> getSkillSupplyDemand(int limit) {
        return skillSupplyDemand.stream().limit(limit).toList();
    }

    @Override
    public List<MarketableSkill> getMarketableSkills(int limitPerPosition) {
        return List.copyOf(marketableSkills);
    }
}
