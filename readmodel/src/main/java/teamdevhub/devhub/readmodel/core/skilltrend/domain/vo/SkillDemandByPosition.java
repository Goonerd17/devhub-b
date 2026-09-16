package teamdevhub.devhub.readmodel.core.skilltrend.domain.vo;

import lombok.Builder;

@Builder
public record SkillDemandByPosition(
        String positionCd,
        String skillCd,
        long count
) {

    public static SkillDemandByPosition of(String positionCd, String skillCd, long count) {
        return SkillDemandByPosition.builder()
                .positionCd(positionCd)
                .skillCd(skillCd)
                .count(count)
                .build();
    }
}
