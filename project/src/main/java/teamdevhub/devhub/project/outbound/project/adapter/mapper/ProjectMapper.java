package teamdevhub.devhub.project.outbound.project.adapter.mapper;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import teamdevhub.devhub.platform.core.common.audit.AuditInfo;
import teamdevhub.devhub.project.core.project.domain.Project;
import teamdevhub.devhub.project.core.project.domain.ProjectLike;
import teamdevhub.devhub.project.core.project.domain.ProjectRequirement;
import teamdevhub.devhub.project.core.project.domain.ProjectSkill;
import teamdevhub.devhub.project.outbound.project.adapter.entity.ProjectEntity;
import teamdevhub.devhub.project.outbound.project.adapter.entity.ProjectLikeEntity;
import teamdevhub.devhub.project.outbound.project.adapter.entity.ProjectRequirementEntity;
import teamdevhub.devhub.project.outbound.project.adapter.entity.ProjectSkillEntity;

public class ProjectMapper {

	
	public static ProjectEntity toEntity(Project project) {
		return ProjectEntity.builder()
				.projectGuid(project.getProjectGuid())
				.attachmentFileGuid(project.getAttachmentFileGuid())
				.imageFileGuid(project.getImageFileGuid())
				.userGuid(project.getUserGuid())
				.recruitmentTypeCd(project.getRecruitmentTypeCd())
				.progressTypeCd(project.getProgressTypeCd())
				.progressRegionCd(project.getProgressRegionCd())
				.username(project.getUsername())
				.category(project.getCategory())
				.title(project.getTitle())
				.content(project.getContent())
				.recruitmentStartDate(project.getRecruitmentStartDate())
				.recruitmentEndDate(project.getRecruitmentEndDate())
				.progressStartDate(project.getProgressStartDate())
				.progressEndDate(project.getProgressEndDate())
				.deleted(project.isDeleted())
				.capacityClosed(project.isCapacityClosed())
				.build();
	}

    public static Project toProject(Project project, List<String> projectSkills, List<ProjectRequirement> projectRequirementList, String likeCount) {
        return Project.builder()
                .projectGuid(project.getProjectGuid())
                .userGuid(project.getUserGuid())
                .username(project.getUsername())
                .category(project.getCategory())
                .title(project.getTitle())
                .content(project.getContent())
                .attachmentFileGuid(project.getAttachmentFileGuid())
				.imageFileGuid(project.getImageFileGuid())
                .recruitmentTypeCd(project.getRecruitmentTypeCd())
                .recruitmentStartDate(project.getRecruitmentStartDate())
                .recruitmentEndDate(project.getRecruitmentEndDate())
                .progressTypeCd(project.getProgressTypeCd())
                .progressRegionCd(project.getProgressRegionCd())
//                .progressPeriod(project.getpro)
                .progressStartDate(project.getProgressStartDate())
                .progressEndDate(project.getProgressEndDate())
                .likeCount(likeCount)
                .projectSkill(projectSkills)
                .projectRequirement(projectRequirementList)
                .auditInfo(project.getAuditInfo())
                .capacityClosed(project.isCapacityClosed())
                .build();
    }

    private static AuditInfo toAuditInfo(ProjectEntity entity) {
        return AuditInfo.of(
                entity.getRegistrantGuid(),
                entity.getRegisteredDate(),
                entity.getModifierGuid(),
                entity.getModifiedDate()
        );
    }
	
    public static Project toProject(ProjectEntity entity) {
        if (entity == null) return null;
        return Project.builder()
                .projectGuid(entity.getProjectGuid())
                .userGuid(entity.getUserGuid())
                .username(entity.getUsername())
                .attachmentFileGuid(entity.getAttachmentFileGuid())
                .imageFileGuid(entity.getImageFileGuid())
                .category(entity.getCategory())
                .title(entity.getTitle())
                .content(entity.getContent())
                .recruitmentTypeCd(entity.getRecruitmentTypeCd())
                .recruitmentStartDate(entity.getRecruitmentStartDate())
                .recruitmentEndDate(entity.getRecruitmentEndDate())
                .progressTypeCd(entity.getProgressTypeCd())
                .progressRegionCd(entity.getProgressRegionCd())
//                .progressPeriod(entity.getpro)
                .progressStartDate(entity.getProgressStartDate())
                .progressEndDate(entity.getProgressEndDate())
                .capacityClosed(entity.isCapacityClosed())
                .auditInfo(toAuditInfo(entity))
                .build();
    }

    public static ProjectRequirement toRequirement(ProjectRequirementEntity entity) {
        if (entity == null) return null;
        return ProjectRequirement.builder()
                .projectRequirementGuid(entity.getProjectRequirementGuid())
                .projectGuid(entity.getProjectGuid())
                .positionCd(entity.getPositionCd())
                .levelCd(entity.getLevelCd())
                .capacity(entity.getCapacity())
                .build();
    }
    
    public static ProjectLike toProjectLike(ProjectLikeEntity entity) {
    	if(entity == null) return null;
    	return ProjectLike.builder()
    			.projectLikeGuid(entity.getProjectLikeGuid())
    			.projectGuid(entity.getProjectGuid())
    			.userGuid(entity.getUserGuid())
    			.build();
    }
    
    public static Map<String, List<String>> skillEntityToMapSkill(List<ProjectSkillEntity> entityList) {
        if (entityList == null) return null;
        return entityList.stream()
                .collect(Collectors.groupingBy(
                		ProjectSkillEntity::getProjectGuid,
                        Collectors.mapping(ProjectSkillEntity::getSkillCd, Collectors.toList())
                ));
    }

    public static Map<String, List<ProjectRequirementEntity>> requirementEntityToMapRequirement(List<ProjectRequirementEntity> entityList) {
        if (entityList == null) return null;
        return entityList.stream()
                .collect(Collectors.groupingBy(ProjectRequirementEntity::getProjectGuid));
    }

    public static Map<String, List<String>> toMapSkill(List<ProjectSkill> entityList) {
        if (entityList == null) return null;
        return entityList.stream()
                .collect(Collectors.groupingBy(
                        ProjectSkill::getProjectGuid,
                        Collectors.mapping(ProjectSkill::getSkillCd, Collectors.toList())
                ));
    }

    public static Map<String, List<ProjectRequirement>> toMapRequirement(List<ProjectRequirement> entityList) {
        if (entityList == null) return null;
        return entityList.stream()
                .collect(Collectors.groupingBy(ProjectRequirement::getProjectGuid));
    }
    

	public static Map<String, String> toMapLikeCount(List<ProjectLike> entityList) {
		if (entityList == null) return null;
		return entityList.stream()
		        .collect(Collectors.groupingBy(
		                ProjectLike::getProjectGuid,
		                Collectors.collectingAndThen(
		                        Collectors.counting(),
		                        count -> String.valueOf(count)
		                )
		        ));
	}

    public static Project toProjectDetail(
            ProjectEntity projectEntity,
            List<ProjectSkillEntity> skillEntities,
            List<ProjectRequirementEntity> requirementEntities,
            String likeCount
    ) {
            if (projectEntity == null) return null;
        return Project.builder()
                .projectGuid(projectEntity.getProjectGuid())
                .userGuid(projectEntity.getUserGuid())
                .username(projectEntity.getUsername())
                .category(projectEntity.getCategory())
                .title(projectEntity.getTitle())
                .content(projectEntity.getContent())
                .recruitmentTypeCd(projectEntity.getRecruitmentTypeCd())
                .recruitmentStartDate(projectEntity.getRecruitmentStartDate())
                .recruitmentEndDate(projectEntity.getRecruitmentEndDate())
                .progressTypeCd(projectEntity.getProgressTypeCd())
                .progressRegionCd(projectEntity.getProgressRegionCd())
                .progressStartDate(projectEntity.getProgressStartDate())
                .progressEndDate(projectEntity.getProgressEndDate())
                .likeCount(likeCount)
                .projectSkill(
                        skillEntities == null ? List.of() :
                                skillEntities.stream()
                                        .map(ProjectSkillEntity::getSkillCd)
                                        .filter(Objects::nonNull)
                                        .distinct()
                                        .toList()
                )
                .projectRequirement(
                        requirementEntities == null ? List.of() :
                                requirementEntities.stream()
                                        .map(ProjectMapper::toRequirement)
                                        .toList()
                )
                .auditInfo(toAuditInfo(projectEntity))
                .capacityClosed(projectEntity.isCapacityClosed())
                .build();
    }

	public static Project toProjectDetail(Project project, List<String> skills, List<ProjectRequirement> requirements, String likeCount) {
		return Project.builder()
                .projectGuid(project.getProjectGuid())
                .userGuid(project.getUserGuid())
                .username(project.getUsername())
                .category(project.getCategory())
                .title(project.getTitle())
                .content(project.getContent())
                .recruitmentTypeCd(project.getRecruitmentTypeCd())
                .recruitmentStartDate(project.getRecruitmentStartDate())
                .recruitmentEndDate(project.getRecruitmentEndDate())
                .progressTypeCd(project.getProgressTypeCd())
                .progressRegionCd(project.getProgressRegionCd())
                .progressStartDate(project.getProgressStartDate())
                .progressEndDate(project.getProgressEndDate())
                .projectSkill(skills)
                .projectRequirement(requirements)
                .auditInfo(project.getAuditInfo())
                .likeCount(likeCount)
                .capacityClosed(project.isCapacityClosed())
                .build();
	}

}
