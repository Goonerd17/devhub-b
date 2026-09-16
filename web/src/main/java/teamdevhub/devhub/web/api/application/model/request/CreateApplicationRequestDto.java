package teamdevhub.devhub.web.api.application.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import teamdevhub.devhub.project.core.application.port.in.command.CreateApplicationCommand;

import java.util.List;

@Getter
public class CreateApplicationRequestDto {

	@NotBlank
	private String requirementGuid;

	@NotEmpty
	private List<AnswerRequestDto> answers;

	@Getter
	public static class AnswerRequestDto {

		@NotBlank
		private String projectApplicationFormGuid;

		@NotBlank
		private String applicationFormGuid;

		@NotBlank
		private String content;

		private String fileGuid;
	}

	public CreateApplicationCommand toCommand(String projectGuid, String applicantGuid) {
		return CreateApplicationCommand.builder()
			.projectGuid(projectGuid)
			.applicantGuid(applicantGuid)
			.requirementGuid(this.requirementGuid)
			.answers(this.answers.stream()
				.map(a -> CreateApplicationCommand.AnswerCommand.builder()
					.projectApplicationFormGuid(a.getProjectApplicationFormGuid())
					.applicationFormGuid(a.getApplicationFormGuid())
					.content(a.getContent())
					.fileGuid(a.getFileGuid())
					.build()
				)
				.toList()
			)
			.build();
	}
}
