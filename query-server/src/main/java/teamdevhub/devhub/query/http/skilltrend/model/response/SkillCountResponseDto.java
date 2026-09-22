package teamdevhub.devhub.query.http.skilltrend.model.response;

import teamdevhub.devhub.query.core.skilltrend.domain.vo.SkillCount;

public record SkillCountResponseDto(
        String skillCd,
        long count
) {

    public static SkillCountResponseDto from(SkillCount skillCount) {
        return new SkillCountResponseDto(skillCount.skillCd(), skillCount.count());
    }
}

