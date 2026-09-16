package teamdevhub.devhub.project.core.project.domain.vo.command;

import lombok.Builder;

@Builder
public record CreateProjectLikeCommand(String projectGuid, String userGuid) {
	
	public static CreateProjectLikeCommand toCreateProjectLikeCommand(String projectGuid, String userGuid) {
		return CreateProjectLikeCommand.builder()
				.projectGuid(projectGuid)
				.userGuid(userGuid)
				.build();
	}

}
