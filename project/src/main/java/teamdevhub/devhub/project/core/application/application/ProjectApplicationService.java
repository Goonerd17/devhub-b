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
import teamdevhub.devhub.platform.core.common.page.PageCommand;
import teamdevhub.devhub.platform.core.common.page.PageResult;
import teamdevhub.devhub.platform.identifier.IdentifierProvider;
import teamdevhub.devhub.project.core.project.domain.ProjectApprovalStatus;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProjectApplicationService implements ProjectApplicationQueryUseCase, ProjectApplicationUseCase {

	private final ApplicationRepository applicationRepository;
	private final IdentifierProvider identifierProvider;

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
	}

	@Override
	@Transactional
	public void approveApplication(ApproveApplicationCommand command) {
		String decisionDate = LocalDate.now().toString();
		applicationRepository.updateApplicationStatus(
			command.applicationGuid(),
			command.resolveStatusCd(),
			command.approverGuid(),
			decisionDate
		);
	}

	@Override
	@Transactional
	public void cancelApplication(String applicationGuid, String applicantGuid) {
		ProjectApplication application = applicationRepository.findApplicationByGuid(applicationGuid);
		application.assertCancelable(applicantGuid);
		applicationRepository.cancelApplication(applicationGuid);
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
