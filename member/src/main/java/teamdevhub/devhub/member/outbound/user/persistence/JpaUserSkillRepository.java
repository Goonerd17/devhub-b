package teamdevhub.devhub.member.outbound.user.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import teamdevhub.devhub.member.outbound.user.adapter.entity.UserSkillEntity;

import java.util.List;
import java.util.Set;

public interface JpaUserSkillRepository  extends JpaRepository<UserSkillEntity, String> {

    List<UserSkillEntity> findByUserGuid(String userGuid);
    List<UserSkillEntity> findByUserGuidIn(List<String> userGuids);
    void deleteByUserGuidAndSkillCdIn(String userGuid, Set<String> skillCds);
}
