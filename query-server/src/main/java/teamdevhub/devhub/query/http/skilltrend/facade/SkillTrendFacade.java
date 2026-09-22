package teamdevhub.devhub.query.http.skilltrend.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teamdevhub.devhub.query.http.skilltrend.model.response.CardInfoResponseDto;
import teamdevhub.devhub.query.http.skilltrend.model.response.MarketableSkillResponseDto;
import teamdevhub.devhub.query.http.skilltrend.model.response.PositionCountResponseDto;
import teamdevhub.devhub.query.http.skilltrend.model.response.ProjectTimelineResponseDto;
import teamdevhub.devhub.query.http.skilltrend.model.response.SkillCountResponseDto;
import teamdevhub.devhub.query.http.skilltrend.model.response.SkillDemandByPositionResponseDto;
import teamdevhub.devhub.query.http.skilltrend.model.response.SkillSupplyDemandResponseDto;
import teamdevhub.devhub.query.http.skilltrend.model.response.SkillTrendResponseDto;
import teamdevhub.devhub.query.core.skilltrend.port.in.usecase.SkillTrendAnalyticsUseCase;
import teamdevhub.devhub.query.core.skilltrend.port.in.usecase.SkillTrendStatisticsUseCase;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SkillTrendFacade {

    private static final int SKILL_LIMIT = 10;
    private static final int POSITION_LIMIT = 10;
    private static final int TIMELINE_MONTHS = 12;
    private static final int SUPPLY_DEMAND_LIMIT = 10;
    private static final int LIMIT_PER_POSITION = 5;

    private final SkillTrendStatisticsUseCase skillTrendStatisticsUseCase;
    private final SkillTrendAnalyticsUseCase skillTrendAnalyticsUseCase;

    public SkillTrendResponseDto getSkillTrendData() {
        CardInfoResponseDto cardInfo = CardInfoResponseDto.from(
                skillTrendStatisticsUseCase.getCardInfo());

        List<SkillCountResponseDto> demandedSkills = skillTrendStatisticsUseCase
                .getDemandedSkills(SKILL_LIMIT)
                .stream()
                .map(SkillCountResponseDto::from)
                .toList();

        List<PositionCountResponseDto> popularPositions = skillTrendStatisticsUseCase
                .getPopularPositions(POSITION_LIMIT)
                .stream()
                .map(PositionCountResponseDto::from)
                .toList();

        List<SkillDemandByPositionResponseDto> skillDemandByPosition = skillTrendAnalyticsUseCase
                .getSkillDemandByPosition(LIMIT_PER_POSITION)
                .stream()
                .map(SkillDemandByPositionResponseDto::from)
                .toList();

        List<ProjectTimelineResponseDto> projectTimeline = skillTrendStatisticsUseCase
                .getProjectTimeline(TIMELINE_MONTHS)
                .stream()
                .map(ProjectTimelineResponseDto::from)
                .toList();

        List<SkillSupplyDemandResponseDto> supplyAndDemand = skillTrendAnalyticsUseCase
                .getSkillSupplyDemand(SUPPLY_DEMAND_LIMIT)
                .stream()
                .map(SkillSupplyDemandResponseDto::from)
                .toList();

        List<MarketableSkillResponseDto> marketableSkills = skillTrendAnalyticsUseCase
                .getMarketableSkills(LIMIT_PER_POSITION)
                .stream()
                .map(MarketableSkillResponseDto::from)
                .toList();

        return SkillTrendResponseDto.of(
                cardInfo,
                demandedSkills,
                popularPositions,
                skillDemandByPosition,
                projectTimeline,
                supplyAndDemand,
                marketableSkills
        );
    }
}

