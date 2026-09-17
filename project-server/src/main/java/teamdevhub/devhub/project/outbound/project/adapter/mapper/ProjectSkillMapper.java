package teamdevhub.devhub.project.outbound.project.adapter.mapper;

import teamdevhub.devhub.project.core.project.domain.ProjectSkill;
import teamdevhub.devhub.project.outbound.project.adapter.entity.ProjectSkillEntity;

public class ProjectSkillMapper {

	public static ProjectSkillEntity toEntity(ProjectSkill projectSkill) {
		return ProjectSkillEntity.builder()
				.projectSkillGuid(projectSkill.getProjectSkillGuid())
				.projectGuid(projectSkill.getProjectGuid())
				.skillCd(projectSkill.getSkillCd())
				.build();
	}
	
	public static ProjectSkill toProjectSkill(ProjectSkillEntity projectSkillEntity) {
		return ProjectSkill.builder()
				.projectSkillGuid(projectSkillEntity.getProjectSkillGuid())
				.projectGuid(projectSkillEntity.getProjectGuid())
				.skillCd(projectSkillEntity.getSkillCd())
				.build();
	}
}
