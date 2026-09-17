package teamdevhub.devhub.project.outbound.project.adapter.mapper;

import teamdevhub.devhub.project.core.project.domain.ProjectRequirement;
import teamdevhub.devhub.project.outbound.project.adapter.entity.ProjectRequirementEntity;

public class ProjectRequirementMapper {
	
	public static ProjectRequirementEntity toEntity(ProjectRequirement projectRequirement) {
		return ProjectRequirementEntity.builder()
				.projectRequirementGuid(projectRequirement.getProjectRequirementGuid())
				.projectGuid(projectRequirement.getProjectGuid())
				.positionCd(projectRequirement.getPositionCd())
				.levelCd(projectRequirement.getLevelCd())
				.capacity(projectRequirement.getCapacity())
				.build();
	}
	
	public static ProjectRequirement toProjectRequirement(ProjectRequirementEntity projectRequirementEntity) {
		return ProjectRequirement.builder()
				.projectRequirementGuid(projectRequirementEntity.getProjectRequirementGuid())
				.projectGuid(projectRequirementEntity.getProjectGuid())
				.positionCd(projectRequirementEntity.getPositionCd())
				.levelCd(projectRequirementEntity.getLevelCd())
				.capacity(projectRequirementEntity.getCapacity())
				.build();
				
	}
}
