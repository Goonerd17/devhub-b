package teamdevhub.devhub.member.core.user.port.out;

import teamdevhub.devhub.member.core.user.domain.vo.skill.UserSkill;

import java.util.Set;

public interface UserSkillRepository {

    void saveAll(Set<UserSkill> skills);
    Set<UserSkill> findByUserGuid(String userGuid);
    void replace(Set<UserSkill> previousSkills, Set<UserSkill> changedSkills);
}