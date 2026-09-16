package teamdevhub.devhub.web.api.skilltrend.model.response;

import teamdevhub.devhub.readmodel.core.skilltrend.domain.vo.SkillCount;

public record SkillCountResponseDto(
        String skillCd,
        long count
) {

    public static SkillCountResponseDto from(SkillCount skillCount) {
        return new SkillCountResponseDto(skillCount.skillCd(), skillCount.count());
    }
}
