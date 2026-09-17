package teamdevhub.devhub.query.core.skilltrend.domain.vo;

import lombok.Builder;

@Builder
public record MarketableSkill(
        String positionCd,
        String skillCd,
        long count
) {

    public static MarketableSkill of(String positionCd, String skillCd, long count) {
        return MarketableSkill.builder()
                .positionCd(positionCd)
                .skillCd(skillCd)
                .count(count)
                .build();
    }
}
