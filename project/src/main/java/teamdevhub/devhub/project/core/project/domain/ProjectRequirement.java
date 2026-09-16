package teamdevhub.devhub.project.core.project.domain;

import lombok.Builder;
import lombok.Getter;
import teamdevhub.devhub.project.core.project.domain.vo.command.CreateProjectRequirementCommand;

@Builder
@Getter
public class ProjectRequirement {
	
	private String projectRequirementGuid;
	
	private String projectGuid;
	
	private String positionCd;
	
	private String levelCd;
	
	private int capacity;
	
	public static ProjectRequirement createProjectRequirement(String projectRequirementGuid, CreateProjectRequirementCommand createProjectRequirementCommand) {
		return ProjectRequirement.builder()
				.projectRequirementGuid(projectRequirementGuid)
				.projectGuid(createProjectRequirementCommand.projectGuid())
				.positionCd(createProjectRequirementCommand.position())
				.levelCd(createProjectRequirementCommand.level())
				.capacity(createProjectRequirementCommand.capacity())
				.build();
	}
	
}
