package teamdevhub.devhub.project.http.application.facade;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import teamdevhub.devhub.project.http.application.model.request.CreateApplicationRequestDto;
import teamdevhub.devhub.project.http.application.model.response.ProjectApplicationAnswerDetailResponseDto;
import teamdevhub.devhub.project.http.application.model.response.ProjectApplicationBasicResponseDto;
import teamdevhub.devhub.project.http.application.model.response.ProjectApplicationDetailResponseDto;
import teamdevhub.devhub.project.http.application.model.response.ProjectApplicationDetailWrapperResponseDto;
import teamdevhub.devhub.project.http.application.model.response.ProjectApplicationListResponseDto;
import teamdevhub.devhub.web.api.model.response.DataApiResponseDto;
import teamdevhub.devhub.project.http.response.PageResponseDto;
import teamdevhub.devhub.project.core.application.domain.ProjectApplication;
import teamdevhub.devhub.project.core.application.domain.ProjectApplicationAnswer;
import teamdevhub.devhub.project.core.application.port.in.command.ApproveApplicationCommand;
import teamdevhub.devhub.project.core.application.port.in.usecase.ProjectApplicationQueryUseCase;
import teamdevhub.devhub.project.core.application.port.in.usecase.ProjectApplicationUseCase;
import teamdevhub.devhub.shared.core.common.page.PageCommand;
import teamdevhub.devhub.shared.core.common.page.PageResult;
import teamdevhub.devhub.project.core.project.domain.Project;
import teamdevhub.devhub.project.core.project.domain.ProjectRequirement;
import teamdevhub.devhub.project.http.project.model.ProjectDetailResponseDto;
import teamdevhub.devhub.project.core.project.port.in.usecase.ProjectUseCase;
import teamdevhub.devhub.web.enums.SuccessCode;

@Service
@RequiredArgsConstructor
public class ProjectApplicationFacade {

	private final ProjectApplicationQueryUseCase projectApplicationQueryUseCase;
	private final ProjectApplicationUseCase projectApplicationUseCase;
	private final ProjectUseCase projectUseCase;

	public DataApiResponseDto<Void> approveApplication(
		String applicationGuid,
		String approverGuid,
		boolean approved
	) {
		projectApplicationUseCase.approveApplication(
			ApproveApplicationCommand.builder()
				.applicationGuid(applicationGuid)
				.approverGuid(approverGuid)
				.approved(approved)
				.build()
		);
		return DataApiResponseDto.successWithoutData(SuccessCode.UPDATE_SUCCESS);
	}

	public DataApiResponseDto<Void> createApplication(
		String projectGuid,
		String applicantGuid,
		CreateApplicationRequestDto requestDto
	) {
		Project project = projectUseCase.getProjectDetail(projectGuid);
		project.assertApplicable(applicantGuid);

		projectApplicationUseCase.createApplication(requestDto.toCommand(projectGuid, applicantGuid));
		return DataApiResponseDto.successWithoutData(SuccessCode.CREATE_SUCCESS);
	}

	public DataApiResponseDto<Void> cancelApplication(
		String applicationGuid,
		String applicantGuid
	) {
		projectApplicationUseCase.cancelApplication(applicationGuid, applicantGuid);
		return DataApiResponseDto.successWithoutData(SuccessCode.UPDATE_SUCCESS);
	}

	public DataApiResponseDto<ProjectApplicationListResponseDto> getApplicationsByProjectGuid(
		String projectGuid,
		PageCommand pageCommand
	) {
		Project project = projectUseCase.getProjectDetail(projectGuid);

		// 모집 포지션별 승인 인원(현재 인원)을 반영하기 위해 requirementGuid별 승인 수를 조회한다.
		List<String> requirementGuids = project.getProjectRequirement() == null
			? List.of()
			: project.getProjectRequirement().stream()
				.map(ProjectRequirement::getProjectRequirementGuid)
				.toList();
		var approvedCountResolver = ProjectDetailResponseDto.approvedCountResolverOf(
			projectApplicationQueryUseCase.countApprovedByRequirementGuids(requirementGuids)
		);
		ProjectDetailResponseDto projectDetailDto = ProjectDetailResponseDto.fromDomain(project, null, null, null, approvedCountResolver);

		PageResult<ProjectApplication> pagedApplications =
			projectApplicationQueryUseCase.getApplicationsByProjectGuid(projectGuid, pageCommand);

		List<ProjectApplicationDetailResponseDto> applicationList = pagedApplications.content().stream()
			.map(ProjectApplicationDetailResponseDto::fromDomain)
			.toList();

		ProjectApplicationListResponseDto responseDto = ProjectApplicationListResponseDto.builder()
			.projectDetailDto(projectDetailDto)
			.applicationList(applicationList)
			.pagination(PageResponseDto.from(pagedApplications))
			.build();

		return DataApiResponseDto.successWithData(SuccessCode.READ_SUCCESS, responseDto);
	}

	public DataApiResponseDto<ProjectApplicationDetailWrapperResponseDto> getApplicationDetail(
		String applicationGuid
	) {
		ProjectApplication application =
			projectApplicationQueryUseCase.getApplicationByGuid(applicationGuid);

		List<ProjectApplicationAnswer> answers =
			projectApplicationQueryUseCase.getAnswersByApplicationGuid(applicationGuid);

		List<ProjectApplicationAnswerDetailResponseDto> answerDtoList = answers.stream()
			.map(ProjectApplicationAnswerDetailResponseDto::fromDomain)
			.toList();

		ProjectApplicationDetailWrapperResponseDto responseDto = ProjectApplicationDetailWrapperResponseDto.builder()
			.projectApplicationBasicDto(ProjectApplicationBasicResponseDto.fromDomain(application))
			.projectApplicationAnswerList(answerDtoList)
			.build();

		return DataApiResponseDto.successWithData(SuccessCode.READ_SUCCESS, responseDto);
	}
}
