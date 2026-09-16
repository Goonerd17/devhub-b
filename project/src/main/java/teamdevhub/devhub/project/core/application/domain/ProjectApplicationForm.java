package teamdevhub.devhub.project.core.application.domain;

import lombok.Builder;
import lombok.Getter;
import teamdevhub.devhub.project.core.application.port.in.command.CreateProjectApplicationFormCommand;

@Builder
@Getter
public class ProjectApplicationForm {
	
	private String projectApplicationFormGuid;
	private String projectGuid;
	private String applicationFormGuid;
	
	public static ProjectApplicationForm createProjectApplicationForm(String projectApplicationFormGuid, CreateProjectApplicationFormCommand createProjectApplicationFormCommand) {
		return ProjectApplicationForm.builder()
				.projectApplicationFormGuid(projectApplicationFormGuid)
				.projectGuid(createProjectApplicationFormCommand.projectGuid())
				.applicationFormGuid(createProjectApplicationFormCommand.applicationFormGuid())
				.build();
	}
}
