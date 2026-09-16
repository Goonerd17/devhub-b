package teamdevhub.devhub.web.api.skilltrend.model.response;

import java.util.List;

public record SkillTrendResponseDto(
        CardInfoResponseDto cardInfo,
        List<SkillCountResponseDto> demandedSkills,
        List<PositionCountResponseDto> popularPositions,
        List<SkillDemandByPositionResponseDto> skillDemandByPosition,
        List<ProjectTimelineResponseDto> projectTimeline,
        List<SkillSupplyDemandResponseDto> supplyAndDemandOfSkills,
        List<MarketableSkillResponseDto> marketableSkills
) {

    public static SkillTrendResponseDto of(
            CardInfoResponseDto cardInfo,
            List<SkillCountResponseDto> demandedSkills,
            List<PositionCountResponseDto> popularPositions,
            List<SkillDemandByPositionResponseDto> skillDemandByPosition,
            List<ProjectTimelineResponseDto> projectTimeline,
            List<SkillSupplyDemandResponseDto> supplyAndDemandOfSkills,
            List<MarketableSkillResponseDto> marketableSkills
    ) {
        return new SkillTrendResponseDto(
                cardInfo,
                demandedSkills,
                popularPositions,
                skillDemandByPosition,
                projectTimeline,
                supplyAndDemandOfSkills,
                marketableSkills
        );
    }
}
