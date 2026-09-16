package teamdevhub.devhub.project.core.application.port.in.command;

import lombok.Builder;

import java.util.List;

@Builder
public record CreateApplicationCommand(
	String projectGuid,
	String applicantGuid,
	String requirementGuid,
	List<AnswerCommand> answers
) {
	@Builder
	public record AnswerCommand(
		String projectApplicationFormGuid,
		String applicationFormGuid,
		String content,
		String fileGuid
	) {}
}
