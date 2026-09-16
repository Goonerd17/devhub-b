package teamdevhub.devhub.readmodel.core.skilltrend.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teamdevhub.devhub.readmodel.core.skilltrend.domain.vo.MarketableSkill;
import teamdevhub.devhub.readmodel.core.skilltrend.domain.vo.SkillDemandByPosition;
import teamdevhub.devhub.readmodel.core.skilltrend.domain.vo.SkillSupplyDemand;
import teamdevhub.devhub.readmodel.core.skilltrend.port.in.usecase.SkillTrendAnalyticsUseCase;
import teamdevhub.devhub.readmodel.core.skilltrend.port.out.LoadSkillDemandPort;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SkillTrendAnalyticsService implements SkillTrendAnalyticsUseCase {

    private final LoadSkillDemandPort loadSkillDemandPort;

    @Override
    public List<SkillDemandByPosition> getSkillDemandByPosition(int limitPerPosition) {
        return loadSkillDemandPort.loadSkillDemandByPosition(limitPerPosition);
    }

    @Override
    public List<SkillSupplyDemand> getSkillSupplyDemand(int limit) {
        return loadSkillDemandPort.loadSkillSupplyDemand(limit);
    }

    @Override
    public List<MarketableSkill> getMarketableSkills(int limitPerPosition) {
        return loadSkillDemandPort.loadMarketableSkills(limitPerPosition);
    }
}
