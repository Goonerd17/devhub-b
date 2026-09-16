package teamdevhub.devhub.project.outbound.project.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import teamdevhub.devhub.platform.identifier.IdentifierProvider;
import teamdevhub.devhub.project.core.project.domain.ProjectSkill;
import teamdevhub.devhub.project.core.project.domain.vo.command.CreateProjectSkillCommand;
import teamdevhub.devhub.project.core.project.port.out.ProjectSkillRepository;
import teamdevhub.devhub.project.outbound.project.adapter.entity.ProjectSkillEntity;
import teamdevhub.devhub.project.outbound.project.adapter.mapper.ProjectSkillMapper;
import teamdevhub.devhub.project.outbound.project.persistence.JpaProjectSkillRepository;

@Component
@RequiredArgsConstructor
public class ProjectSkillAdapter implements ProjectSkillRepository {
	
	private final IdentifierProvider identifierProvider;
	private final JpaProjectSkillRepository jpaProjectSkillRepository;
	
	@Override
	public void saveAll(Set<CreateProjectSkillCommand> skills) {
		if(skills.isEmpty()) {
			return;
		}

		List<ProjectSkillEntity> projectSkillEntityList = skills.stream()
				.map(projectSkillCommand -> {
					String projectSkillGuid = identifierProvider.generateIdentifier();
					ProjectSkill projectSkill =  ProjectSkill.createProjectSkill(projectSkillGuid, projectSkillCommand);
					return ProjectSkillMapper.toEntity(projectSkill);
				})
				.toList();
		jpaProjectSkillRepository.saveAll(projectSkillEntityList);
		
	}

	@Override
	public List<String> selectProjectGuidBySkillCd(List<String> skillCodeList) {
		List<String> projectGuids = new ArrayList<>();
		if(skillCodeList == null) {
			return projectGuids;
		}
		return jpaProjectSkillRepository.findBySkillCdList(skillCodeList);
	}

	@Override
	public List<ProjectSkill> findByProjectGuids(Set<String> projectGuids) {
		List<ProjectSkillEntity> entityList = jpaProjectSkillRepository.findByProjectGuids(projectGuids);
		return entityList.stream()
				.map(ProjectSkillMapper::toProjectSkill)
				.toList();
	}

	@Override
	public List<ProjectSkill> findByProjectGuid(String projectGuid) {
		List<ProjectSkillEntity> entityList = jpaProjectSkillRepository.findByProjectGuid(projectGuid);
		return entityList.stream()
				.map(ProjectSkillMapper::toProjectSkill)
				.toList();
	}

	@Override
	public void deleteByProjectGuid(String projectGuid) {
		jpaProjectSkillRepository.deleteAllByProjectGuid(projectGuid);
	}

}
