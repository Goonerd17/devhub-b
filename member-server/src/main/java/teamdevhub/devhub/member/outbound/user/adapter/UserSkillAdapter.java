package teamdevhub.devhub.member.outbound.user.adapter;

import org.springframework.stereotype.Component;
import teamdevhub.devhub.member.outbound.user.adapter.entity.UserSkillEntity;
import teamdevhub.devhub.member.outbound.user.adapter.mapper.UserSkillMapper;
import teamdevhub.devhub.member.outbound.user.persistence.JpaUserSkillRepository;
import teamdevhub.devhub.member.core.user.domain.vo.skill.UserSkill;
import teamdevhub.devhub.member.core.user.port.out.UserSkillRepository;
import teamdevhub.devhub.shared.identifier.IdentifierProvider;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserSkillAdapter extends UserRelationAdapter<UserSkill> implements UserSkillRepository {

    private final JpaUserSkillRepository jpaUserSkillRepository;

    public UserSkillAdapter(JpaUserSkillRepository jpaUserSkillRepository,
                            IdentifierProvider identifierProvider) {
        super(identifierProvider);
        this.jpaUserSkillRepository = jpaUserSkillRepository;
    }

    @Override
    public void saveAll(Set<UserSkill> skills) {
        if (skills.isEmpty()) {
            return;
        }
        List<UserSkillEntity> entities = skills.stream()
                .map(skill -> UserSkillMapper.toEntity(identifierProvider.generateIdentifier(), skill))
                .toList();
        jpaUserSkillRepository.saveAll(entities);
    }

    @Override
    public Set<UserSkill> findByUserGuid(String userGuid) {
        return jpaUserSkillRepository.findByUserGuid(userGuid)
                .stream()
                .map(UserSkillMapper::toRecord)
                .collect(Collectors.toSet());
    }

    @Override
    public void replace(Set<UserSkill> previousSkills, Set<UserSkill> changedSkills) {
        if (changedSkills == null || changedSkills.isEmpty()) {
            return;
        }
        syncItems(previousSkills, changedSkills);
    }

    @Override
    protected String extractUserGuid(Set<UserSkill> items) {
        return items.iterator().next().userGuid();
    }

    @Override
    protected Set<String> extractCodes(Set<UserSkill> items) {
        return items.stream().map(UserSkill::skillCd).collect(Collectors.toSet());
    }

    @Override
    protected void deleteByUserGuidAndCodes(String userGuid, Set<String> codes) {
        if (!codes.isEmpty()) {
            jpaUserSkillRepository.deleteByUserGuidAndSkillCdIn(userGuid, codes);
        }
    }

    @Override
    protected void insertByUserGuidAndCodes(String userGuid, Set<String> codes) {
        if (codes.isEmpty()) {
            return;
        }
        List<UserSkillEntity> entities = codes.stream()
                .map(code -> UserSkillEntity.builder()
                        .userSkillGuid(identifierProvider.generateIdentifier())
                        .userGuid(userGuid)
                        .skillCd(code)
                        .build())
                .toList();
        jpaUserSkillRepository.saveAll(entities);
    }
}
