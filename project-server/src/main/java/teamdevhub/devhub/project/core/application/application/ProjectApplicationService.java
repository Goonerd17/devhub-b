package teamdevhub.devhub.project.core.application.application;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import teamdevhub.devhub.project.core.application.domain.ProjectApplication;
import teamdevhub.devhub.project.core.application.domain.ProjectApplicationAnswer;
import teamdevhub.devhub.project.core.application.domain.ProjectApplicationScore;
import teamdevhub.devhub.project.core.application.port.in.command.ApproveApplicationCommand;
import teamdevhub.devhub.project.core.application.port.in.command.CreateApplicationCommand;
import teamdevhub.devhub.project.core.application.port.in.usecase.ProjectApplicationQueryUseCase;
import teamdevhub.devhub.project.core.application.port.in.usecase.ProjectApplicationUseCase;
import teamdevhub.devhub.project.core.application.port.out.ApplicationRepository;
import teamdevhub.devhub.shared.core.common.page.PageCommand;
import teamdevhub.devhub.shared.core.common.page.PageResult;
import teamdevhub.devhub.shared.identifier.IdentifierProvider;
import teamdevhub.devhub.project.core.project.domain.ProjectApprovalStatus;
import teamdevhub.devhub.project.outbound.event.ProjectEventOutbox;
import teamdevhub.devhub.shared.event.IntegrationEvent;
import teamdevhub.devhub.shared.event.project.ProjectApplicationSubmittedPayload;
import teamdevhub.devhub.shared.event.project.ProjectApplicationStatusChangedPayload;
import teamdevhub.devhub.shared.event.project.ProjectApplicationCancelledPayload;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProjectApplicationService implements ProjectApplicationQueryUseCase, ProjectApplicationUseCase {

	private final ApplicationRepository applicationRepository;
	private final IdentifierProvider identifierProvider;
	private final ProjectEventOutbox projectEventOutbox;

	@Override
	@Transactional
	public void createApplication(CreateApplicationCommand command) {
		String applicationGuid = identifierProvider.generateIdentifier();

		ProjectApplication application = ProjectApplication.builder()
			.applicationGuid(applicationGuid)
			.requirementGuid(command.requirementGuid())
			.applicantGuid(command.applicantGuid())
			.statusCd(ProjectApprovalStatus.PENDING.getCode())
			.isCanceled(false)
			.build();

		applicationRepository.saveApplication(application);

		List<ProjectApplicationAnswer> answers = command.answers().stream()
			.map(answer -> ProjectApplicationAnswer.builder()
				.projectApplicationFormGuid(answer.projectApplicationFormGuid())
				.applicationAnswerGuid(identifierProvider.generateIdentifier())
				.applicationGuid(applicationGuid)
				.applicationFormGuid(answer.applicationFormGuid())
				.projectGuid(command.projectGuid())
				.content(answer.content())
				.fileGuid(answer.fileGuid())
				.build()
			)
			.toList();

		applicationRepository.saveAnswers(answers);
		projectEventOutbox.append(new IntegrationEvent(
				UUID.randomUUID().toString(), "project.application.submitted", 1, Instant.now(),
				"project-application", applicationGuid, null, null,
				new ProjectApplicationSubmittedPayload(command.projectGuid(), applicationGuid,
						command.requirementGuid(), command.applicantGuid())));
	}

	@Override
	@Transactional
	public void approveApplication(ApproveApplicationCommand command) {
		ProjectApplication application = applicationRepository.findApplicationByGuid(command.applicationGuid());
		String decisionDate = LocalDate.now().toString();
		applicationRepository.updateApplicationStatus(
			command.applicationGuid(),
			command.resolveStatusCd(),
			command.approverGuid(),
			decisionDate
		);
		projectEventOutbox.append(new IntegrationEvent(
				UUID.randomUUID().toString(), "project.application.status-changed", 1, Instant.now(),
				"project-application", command.applicationGuid(), null, null,
				new ProjectApplicationStatusChangedPayload(command.applicationGuid(), application.getProjectGuid(), application.getApplicantGuid(),
						command.resolveStatusCd(), command.approverGuid())));
	}

	@Override
	@Transactional
	public void cancelApplication(String applicationGuid, String applicantGuid) {
		ProjectApplication application = applicationRepository.findApplicationByGuid(applicationGuid);
		application.assertCancelable(applicantGuid);
		applicationRepository.cancelApplication(applicationGuid);
		projectEventOutbox.append(new IntegrationEvent(
				UUID.randomUUID().toString(), "project.application.cancelled", 1, Instant.now(),
				"project-application", applicationGuid, null, null,
				new ProjectApplicationCancelledPayload(applicationGuid, application.getProjectGuid(),
						application.getApplicantGuid())));
	}

	@Override
	public Map<String, Long> countApprovedByRequirementGuids(List<String> requirementGuids) {
		return applicationRepository.countApprovedByRequirementGuids(requirementGuids);
	}

	@Override
	public PageResult<ProjectApplication> getApplicationsByProjectGuid(String projectGuid, PageCommand pageCommand) {
		return applicationRepository.findApplicationsByProjectGuid(projectGuid, pageCommand);
	}

	@Override
	public ProjectApplication getApplicationByGuid(String applicationGuid) {
		return applicationRepository.findApplicationByGuid(applicationGuid);
	}

	@Override
	public List<ProjectApplicationAnswer> getAnswersByApplicationGuid(String applicationGuid) {
		return applicationRepository.findAnswersByApplicationGuid(applicationGuid);
	}

	@Override
	public PageResult<ProjectApplication> findByApplicantGuid(String userGuid, PageCommand pageCommand) {
		return applicationRepository.findByApplicantGuid(userGuid, pageCommand);
	}

	@Override
	public List<ProjectApplicationScore> findAcceptedByProjectGuid(String projectGuid) {
		return applicationRepository.findAcceptedByProjectGuid(projectGuid);
	}
}
