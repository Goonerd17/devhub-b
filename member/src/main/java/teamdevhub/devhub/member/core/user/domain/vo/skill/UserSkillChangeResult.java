package teamdevhub.devhub.member.core.user.domain.vo.skill;

import lombok.Builder;

import java.util.Set;

@Builder
public record UserSkillChangeResult(boolean changed, Set<UserSkill> previousSkills, Set<UserSkill> changedSkills) {

    public static UserSkillChangeResult changed(Set<UserSkill> previousSkills, Set<UserSkill> changedSkills) {
        return new UserSkillChangeResult(true, Set.copyOf(previousSkills), Set.copyOf(changedSkills));
    }

    public static UserSkillChangeResult unchanged(Set<UserSkill> unchangedSkills) {
        return new UserSkillChangeResult(false, Set.copyOf(unchangedSkills), Set.copyOf(unchangedSkills));
    }
}