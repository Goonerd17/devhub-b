package teamdevhub.devhub.project.http.project.model;

import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.experimental.SuperBuilder;
import teamdevhub.devhub.project.core.project.domain.Project;

@Getter
@SuperBuilder
public class ProjectDetailWithFormResponseDto extends ProjectDetailResponseDto {

	private String email;
	private List<ProjectApplicationFormResponseDto> applicationFormList;
	private List<ProjectApplicationFormResponseDto> additionalFormList;
	private String imageFileName;
	private String attachmentFileName;

	public static ProjectDetailWithFormResponseDto fromDomain(Project project, String email, List<ProjectApplicationFormResponseDto> applicationFormList, List<ProjectApplicationFormResponseDto> additionalFormList
			, String imageFileName, String attachmentFilename) {
		return fromDomain(project, email, applicationFormList, additionalFormList, imageFileName, attachmentFilename, Map.of());
	}

	// approvedCountByRequirement: requirementGuid -> 승인된 지원자 수 - 모집 포지션의 현재 인원을 계산하기 위해 주입한다.
	public static ProjectDetailWithFormResponseDto fromDomain(Project project, String email, List<ProjectApplicationFormResponseDto> applicationFormList, List<ProjectApplicationFormResponseDto> additionalFormList
			, String imageFileName, String attachmentFilename, Map<String, Long> approvedCountByRequirement) {
		ProjectDetailWithFormResponseDtoBuilder<?, ?> builder = ProjectDetailWithFormResponseDto.builder();
		fillBase(builder, project);
		return builder
			.email(email)
			.skillList(project.getProjectSkill())
			.positionList(project.getProjectRequirement().stream()
				.map(requirement -> PositionDto.fromDomain(requirement, approvedCountByRequirement.getOrDefault(requirement.getProjectRequirementGuid(), 0L)))
				.toList())
			.likeCount(project.getLikeCount())
			.capacityClosed(project.isCapacityClosed())
			.recruitStatus(project.getRecruitStatus())
			.applicationFormList(applicationFormList)
			.additionalFormList(additionalFormList)
			.imageFileName(imageFileName)
			.attachmentFileName(attachmentFilename)
			.build();
	}
}
