package teamdevhub.devhub.project.core.project.domain;

import lombok.Builder;
import lombok.Getter;
import teamdevhub.devhub.project.core.project.domain.vo.command.CreateProjectLikeCommand;

@Builder
@Getter
public class ProjectLike {
	private String projectLikeGuid;
	
    private String projectGuid;
	
    private String userGuid;

	public static ProjectLike createProjectLike(CreateProjectLikeCommand createProjectLikeCommand,
			String projectLikeGuid) {
		return ProjectLike.builder()
				.projectLikeGuid(projectLikeGuid)
				.projectGuid(createProjectLikeCommand.projectGuid())
				.userGuid(createProjectLikeCommand.userGuid())
				.build();
	}
    
}
