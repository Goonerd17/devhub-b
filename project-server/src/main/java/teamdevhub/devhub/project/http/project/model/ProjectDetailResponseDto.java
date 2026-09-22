package teamdevhub.devhub.project.http.project.model;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import teamdevhub.devhub.project.core.project.domain.Project;
import teamdevhub.devhub.project.core.project.domain.ProjectRequirement;

@Getter
@SuperBuilder
@NoArgsConstructor
public class ProjectDetailResponseDto extends ProjectBasicResponseDto {

	private String nickName;
	private String userFileGuid;
	private String mannerDegree;
	private List<String> skillList;
	private List<PositionDto> positionList;
	private String likeCount;
	private Boolean capacityClosed;
	private Boolean projectLiked;
	private String recruitStatus;

	public static ProjectDetailResponseDto fromDomain(Project project, String imageFileUrl, Boolean projectLiked) {
		return fromDomain(project, imageFileUrl, projectLiked, null);
	}

	public static ProjectDetailResponseDto fromDomain(Project project, String imageFileUrl, Boolean projectLiked, String userFileGuid) {
		return fromDomain(project, imageFileUrl, projectLiked, userFileGuid, requirementGuid -> 0L);
	}

	// approvedCountResolver: requirementGuid -> 승인된(취소되지 않은) 지원자 수 - 모집 포지션의 현재 인원을 계산하기 위해 주입한다.
	public static ProjectDetailResponseDto fromDomain(
		Project project,
		String imageFileUrl,
		Boolean projectLiked,
		String userFileGuid,
		Function<String, Long> approvedCountResolver
	) {
		ProjectDetailResponseDtoBuilder<?, ?> builder = ProjectDetailResponseDto.builder();
		fillBase(builder, project);
		List<PositionDto> positionDtoList = null;
		if (project.getProjectRequirement() != null) {
			positionDtoList = project.getProjectRequirement().stream()
				.map(requirement -> PositionDto.fromDomain(requirement, approvedCountResolver.apply(requirement.getProjectRequirementGuid())))
				.toList();
		}

		return builder
			.nickName(project.getUsername())
			.userFileGuid(userFileGuid)
			.skillList(project.getProjectSkill())
			.positionList(positionDtoList)
			.likeCount(project.getLikeCount())
			.capacityClosed(project.isCapacityClosed())
			.projectLiked(projectLiked)
			.recruitStatus(project.getRecruitStatus())
			.build();
	}

	public static Function<String, Long> approvedCountResolverOf(Map<String, Long> approvedCountByRequirement) {
		return requirementGuid -> approvedCountByRequirement.getOrDefault(requirementGuid, 0L);
	}

	@Getter
	@SuperBuilder
	@NoArgsConstructor
	public static class PositionDto {
		private String requirementGuid;
		private String position;
		private int capacity;
		private String level;
		private long currentCount;
		private boolean full;

		public static PositionDto fromDomain(ProjectRequirement requirement) {
			return fromDomain(requirement, 0L);
		}

		public static PositionDto fromDomain(ProjectRequirement requirement, long approvedCount) {
			return PositionDto.builder()
				.requirementGuid(requirement.getProjectRequirementGuid())
				.position(requirement.getPositionCd())
				.capacity(requirement.getCapacity())
				.level(requirement.getLevelCd())
				.currentCount(approvedCount)
				.full(approvedCount >= requirement.getCapacity())
				.build();
		}
	}
}

