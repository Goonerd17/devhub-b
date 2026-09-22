package teamdevhub.devhub.query.http.skilltrend.model.response;

import teamdevhub.devhub.query.core.skilltrend.domain.vo.SkillDemandByPosition;

public record SkillDemandByPositionResponseDto(
        String positionCd,
        String skillCd,
        long count
) {

    public static SkillDemandByPositionResponseDto from(SkillDemandByPosition skillDemandByPosition) {
        return new SkillDemandByPositionResponseDto(
                skillDemandByPosition.positionCd(),
                skillDemandByPosition.skillCd(),
                skillDemandByPosition.count()
        );
    }
}

