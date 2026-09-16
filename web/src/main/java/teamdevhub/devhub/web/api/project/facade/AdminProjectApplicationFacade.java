package teamdevhub.devhub.web.api.project.facade;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import teamdevhub.devhub.web.api.application.model.request.CreateApplicationRequestDto;
import teamdevhub.devhub.web.api.application.model.response.AdminProjectApplicationBasicResponseDto;
import teamdevhub.devhub.web.api.application.model.response.AdminProjectApplicationListResponseDto;
import teamdevhub.devhub.web.api.application.model.response.ProjectApplicationAnswerDetailResponseDto;
import teamdevhub.devhub.web.api.application.model.response.ProjectApplicationBasicResponseDto;
import teamdevhub.devhub.web.api.application.model.response.ProjectApplicationDetailWrapperResponseDto;
import teamdevhub.devhub.web.api.web.model.response.DataApiResponseDto;
import teamdevhub.devhub.web.api.web.model.response.PageResponseDto;
import teamdevhub.devhub.project.core.application.domain.ProjectApplication;
import teamdevhub.devhub.project.core.application.domain.ProjectApplicationAnswer;
import teamdevhub.devhub.project.core.application.port.in.command.ApproveApplicationCommand;
import teamdevhub.devhub.project.core.application.port.in.command.SearchAdminProjectApplicationCommand;
import teamdevhub.devhub.project.core.application.port.in.usecase.AdminProjectApplicationUseCase;
import teamdevhub.devhub.project.core.application.port.in.usecase.ProjectApplicationUseCase;
import teamdevhub.devhub.platform.core.common.page.PageCommand;
import teamdevhub.devhub.platform.core.common.page.PageResult;
import teamdevhub.devhub.web.shared.enums.SuccessCode;

@Service
@RequiredArgsConstructor
public class AdminProjectApplicationFacade {

	private final AdminProjectApplicationUseCase adminProjectApplicationUseCase;
	private final ProjectApplicationUseCase projectApplicationUseCase;

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
		projectApplicationUseCase.createApplication(requestDto.toCommand(projectGuid, applicantGuid));
		return DataApiResponseDto.successWithoutData(SuccessCode.CREATE_SUCCESS);
	}

	public DataApiResponseDto<AdminProjectApplicationListResponseDto> getApplicationsByProjectGuid(
		SearchAdminProjectApplicationCommand searchAdminProjectApplicationCommand,
		PageCommand pageCommand
	) {
		PageResult<ProjectApplication> pagedApplications =
				adminProjectApplicationUseCase.getApplicationsByProjectGuid(searchAdminProjectApplicationCommand, pageCommand);

		List<AdminProjectApplicationBasicResponseDto> applicationList = pagedApplications.content().stream()
			.map(AdminProjectApplicationBasicResponseDto::fromDomain)
			.toList();

		AdminProjectApplicationListResponseDto responseDto = AdminProjectApplicationListResponseDto.builder()
			.applicantList(applicationList)
			.pagination(PageResponseDto.from(pagedApplications))
			.build();

		return DataApiResponseDto.successWithData(SuccessCode.READ_SUCCESS, responseDto);
	}

	public DataApiResponseDto<ProjectApplicationDetailWrapperResponseDto> getApplicationDetail(
		String applicationGuid
	) {
		ProjectApplication application =
				adminProjectApplicationUseCase.getApplicationByGuid(applicationGuid);

		List<ProjectApplicationAnswer> answers =
				adminProjectApplicationUseCase.getAnswersByApplicationGuid(applicationGuid);

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
