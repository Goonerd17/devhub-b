package teamdevhub.devhub.readmodel.core.skilltrend.domain.vo;

import lombok.Builder;

@Builder
public record SkillCount(
        String skillCd,
        long count
) {

    public static SkillCount of(String skillCd, long count) {
        return SkillCount.builder()
                .skillCd(skillCd)
                .count(count)
                .build();
    }
}
