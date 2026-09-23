package teamdevhub.devhub.project.http.project.facade;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import teamdevhub.devhub.project.http.project.model.ProjectApplicationFormResponseDto;
import teamdevhub.devhub.project.core.application.domain.ProjectApplication;
import teamdevhub.devhub.project.core.application.domain.ProjectApplicationForm;
import teamdevhub.devhub.project.core.application.domain.ProjectApplicationScore;
import teamdevhub.devhub.project.core.application.port.in.usecase.ProjectApplicationQueryUseCase;
import teamdevhub.devhub.project.core.application.port.in.usecase.ProjectApplicationUseCase;
import teamdevhub.devhub.shared.security.CurrentUserPrincipal;
import teamdevhub.devhub.project.core.port.out.MemberEmailPort;
import teamdevhub.devhub.project.core.port.out.ApplicationFormPort;
import teamdevhub.devhub.shared.form.ApplicationFormView;
import teamdevhub.devhub.shared.core.common.exception.BusinessRuleException;
import teamdevhub.devhub.shared.core.common.page.PageCommand;
import teamdevhub.devhub.shared.core.common.page.PageResult;
import teamdevhub.devhub.project.core.port.out.MediaMetadataPort;
import teamdevhub.devhub.project.core.project.domain.Project;
import teamdevhub.devhub.project.core.project.domain.ProjectLike;
import teamdevhub.devhub.project.core.project.domain.ProjectRequirement;
import teamdevhub.devhub.project.core.project.domain.vo.command.CreateProjectCommand;
import teamdevhub.devhub.project.core.project.domain.vo.command.CreateProjectLikeCommand;
import teamdevhub.devhub.project.core.project.domain.vo.command.UpdateProjectCommand;
import teamdevhub.devhub.project.core.project.port.in.command.SearchProjectListCommand;
import teamdevhub.devhub.project.http.project.model.ProjectDetailResponseDto;
import teamdevhub.devhub.project.http.project.model.ProjectDetailWithFormResponseDto;
import teamdevhub.devhub.project.http.project.model.UserProjectResponseDto;
import teamdevhub.devhub.project.core.project.port.in.usecase.ProjectApplicationFormUseCase;
import teamdevhub.devhub.project.core.project.port.in.usecase.ProjectLikeUseCase;
import teamdevhub.devhub.project.core.project.port.in.usecase.ProjectUseCase;
import teamdevhub.devhub.shared.member.MemberProjectOwner;
import teamdevhub.devhub.project.core.port.out.ProjectMemberPort;
import teamdevhub.devhub.shared.enums.ErrorCode;

@Service
@RequiredArgsConstructor
public class ProjectFacade {
	
	private final ProjectUseCase projectUseCase;
	private final ApplicationFormPort applicationFormGateway;
	private final ProjectMemberPort memberProjectOwnerQuery;
	private final ProjectApplicationFormUseCase projectApplicationFormUseCase;
	private final MediaMetadataPort mediaFileMetadataQuery;
	private final ProjectLikeUseCase projectLikeUseCase;
	private final ProjectApplicationUseCase projectApplicationUseCase;
	private final ProjectApplicationQueryUseCase projectApplicationQueryUseCase;
	private final MemberEmailPort memberEmailQuery;

	public PageResult<ProjectDetailResponseDto> getProjectList(SearchProjectListCommand projectListSearchRequestCommand, PageCommand pageCommand, CurrentUserPrincipal user) {
		PageResult<Project> pagedProjectList = projectUseCase.getProjectList(projectListSearchRequestCommand, pageCommand);
		List<ProjectDetailResponseDto> projectDetailResponseDtoList = new ArrayList<>();
		if(user == null) {
			projectDetailResponseDtoList = pagedProjectList.content().stream()
            .map(project -> ProjectDetailResponseDto.fromDomain(project, null, false))
            .toList();
		} else {
	        projectDetailResponseDtoList = pagedProjectList.content().stream()
            .map(project -> {
            	ProjectLike projectLike = projectLikeUseCase.findByProjectGuidAndUserGuid(project.getProjectGuid(), user.userGuid());
            	boolean isProjectLiked = projectLike != null;
                return ProjectDetailResponseDto.fromDomain(project, null, isProjectLiked);
            })
            .toList();
		}

        return PageResult.of(projectDetailResponseDtoList, pagedProjectList.page(), pagedProjectList.size(), pagedProjectList.totalElements());
	}
	
	public void createProject(CreateProjectCommand createProjectCommand) {
		MemberProjectOwner user = memberProjectOwnerQuery.findProjectOwner(createProjectCommand.userGuid());
		List<String> additionalFormGuidList = applicationFormGateway.create(createProjectCommand.additionalFormList());
		createProjectCommand.applicationFormList().addAll(additionalFormGuidList);
		projectUseCase.createProject(createProjectCommand, user.displayName());
	}

	public ProjectDetailResponseDto getProjectDetail(String projectGuid, CurrentUserPrincipal user) {
		String imageFileUrl = null;
        Project project = projectUseCase.getProjectDetail(projectGuid);
        if(project.getImageFileGuid() != null && !project.getImageFileGuid().isBlank()) {
			imageFileUrl = mediaFileMetadataQuery.findMetadata(project.getImageFileGuid()).path();
        }
		String userFileGuid = memberProjectOwnerQuery.findProjectOwner(project.getUserGuid()).profileImageGuid();
        var approvedCountResolver = ProjectDetailResponseDto.approvedCountResolverOf(resolveApprovedCountByRequirement(project));
        if(user == null) {
            	return ProjectDetailResponseDto.fromDomain(project, imageFileUrl, false, userFileGuid, approvedCountResolver);
		} else {
			ProjectLike projectLike = projectLikeUseCase.findByProjectGuidAndUserGuid(project.getProjectGuid(), user.userGuid());
	    	boolean isProjectLiked = projectLike != null;
            return ProjectDetailResponseDto.fromDomain(project, imageFileUrl, isProjectLiked, userFileGuid, approvedCountResolver);
		}
	}

	// 프로젝트의 모집 포지션(requirement)별 승인된 지원자 수 - 현재 모집인원 계산용
	private Map<String, Long> resolveApprovedCountByRequirement(Project project) {
		if (project.getProjectRequirement() == null || project.getProjectRequirement().isEmpty()) {
			return Map.of();
		}
		List<String> requirementGuids = project.getProjectRequirement().stream()
			.map(ProjectRequirement::getProjectRequirementGuid)
			.toList();
		return projectApplicationQueryUseCase.countApprovedByRequirementGuids(requirementGuids);
	}

	public void deleteProject(String projectGuid, CurrentUserPrincipal authenticatedUser) {
		Project project = projectUseCase.getProjectDetail(projectGuid);
		if(!("ADMIN".equals(authenticatedUser.roleName()) || project.getUserGuid().equals(authenticatedUser.userGuid()))) {
			throw BusinessRuleException.of(ErrorCode.DELETE_FAIL);
		}
		// 프로젝트 지원자 조회 후 지원자가 있으면 return, 지원자 없으면 continue??
		// 삭제해야할 신청폼 목록 반환?
		List<String> deleteApplicationFormGuids =  projectUseCase.deleteProject(projectGuid);
		applicationFormGateway.deleteApplicationForms(deleteApplicationFormGuids);
	}

	public void updateProject(String projectGuid, UpdateProjectCommand updateProjectCommand, CurrentUserPrincipal authenticatedUser) {
		Project project = projectUseCase.getProjectDetail(projectGuid);
		if(!("ADMIN".equals(authenticatedUser.roleName()) || project.getUserGuid().equals(authenticatedUser.userGuid()))) {
			throw BusinessRuleException.of(ErrorCode.UPDATE_FAIL);
		}
		List<String> additionalFormGuidList = applicationFormGateway.create(updateProjectCommand.additionalFormList());
		updateProjectCommand.applicationFormList().addAll(additionalFormGuidList);
		List<String> deleteApplicationFormGuids = projectUseCase.updateProject(projectGuid, updateProjectCommand);
		applicationFormGateway.deleteApplicationForms(deleteApplicationFormGuids);
	}

	public ProjectDetailWithFormResponseDto getProjectDetailWithForm(String projectGuid) {
		Project project = projectUseCase.getProjectDetail(projectGuid);
		// 소셜 로그인 전용 회원은 이메일 자격증명이 없을 수 있으므로 없으면 null로 응답한다 (BoardService.detailBoard와 동일한 패턴)
		String email = memberEmailQuery.findEmail(project.getUserGuid()).orElse(null);

		List<ProjectApplicationForm> projectForms = projectApplicationFormUseCase.findByProjectGuid(projectGuid);
		List<String> applicationFormGuids = projectForms.stream()
				.map(ProjectApplicationForm::getApplicationFormGuid)
				.toList();

		Map<String, String> applicationFormGuidToProjectFormGuid = projectForms.stream()
				.collect(Collectors.toMap(
						ProjectApplicationForm::getApplicationFormGuid,
						ProjectApplicationForm::getProjectApplicationFormGuid
				));

		List<ApplicationFormView> standardForms = applicationFormGateway.findStandard(applicationFormGuids);
		List<ApplicationFormView> customForms = applicationFormGateway.findCustomized(applicationFormGuids);

		List<ProjectApplicationFormResponseDto> applicationFormResponseList = standardForms.stream()
				.map(form -> ProjectApplicationFormResponseDto.builder()
						.projectApplicationFormGuid(applicationFormGuidToProjectFormGuid.get(form.applicationFormGuid()))
						.applicationFormGuid(form.applicationFormGuid())
						.typeCd(form.typeCd())
						.title(form.title())
						.helpText(form.helpText())
						.isCustomized(false)
						.isUsed(form.used())
						.build())
				.toList();

		List<ProjectApplicationFormResponseDto> additionalFormResponseList = customForms.stream()
				.map(cmd -> ProjectApplicationFormResponseDto.builder()
						.projectApplicationFormGuid(applicationFormGuidToProjectFormGuid.get(cmd.applicationFormGuid()))
						.applicationFormGuid(cmd.applicationFormGuid())
						.typeCd(cmd.typeCd())
						.title(cmd.title())
						.helpText(cmd.helpText())
						.isCustomized(true)
						.isUsed(cmd.used())
						.itemList(cmd.itemList())
						.build())
				.toList();
		
		String imageFileName = null;
		String attachmentFileName = null;
		if(project.getImageFileGuid() != null && !project.getImageFileGuid().isBlank()) {
			imageFileName = mediaFileMetadataQuery.findMetadata(project.getImageFileGuid()).originalName();
        }
		if(project.getAttachmentFileGuid() != null && !project.getAttachmentFileGuid().isBlank()) {
			attachmentFileName = mediaFileMetadataQuery.findMetadata(project.getAttachmentFileGuid()).originalName();
        }
		Map<String, Long> approvedCountByRequirement = resolveApprovedCountByRequirement(project);
		return ProjectDetailWithFormResponseDto.fromDomain(project, email, applicationFormResponseList, additionalFormResponseList, imageFileName, attachmentFileName, approvedCountByRequirement);
	}

	public void toggleProjectLike(CreateProjectLikeCommand createProjectLikeCommand) {
		projectLikeUseCase.toggleProjectLike(createProjectLikeCommand);
	}

	public PageResult<UserProjectResponseDto> getUserProjects(String userGuid, PageCommand pageCommand) {
		PageResult<Project> pagedProjectList = projectUseCase.getUserProjects(userGuid, pageCommand);
		List<UserProjectResponseDto> userProjectResponseDtoList = pagedProjectList.content().stream()
	            .map(item -> {
	            	PageResult<ProjectApplication> pagedApplicatgionList = projectApplicationQueryUseCase.getApplicationsByProjectGuid(item.getProjectGuid(), new PageCommand(0, Integer.MAX_VALUE));
	            	Project project = projectUseCase.getProjectDetail(item.getProjectGuid());
	            	return UserProjectResponseDto.fromDomain(project, pagedApplicatgionList.content().stream().map(application -> ProjectApplicationScore.toApplicationWithScore(application, 0.0)).toList(), null);
	            })
	            .toList();
		return PageResult.of(userProjectResponseDtoList, pagedProjectList.page(), pagedProjectList.size(), pagedProjectList.totalElements());
	}

	public PageResult<UserProjectResponseDto> getUserLikeProjects(String userGuid, PageCommand pageCommand) {
		PageResult<ProjectLike> pagedLikeProjectList = projectLikeUseCase.findByUserGuid(userGuid, pageCommand);
		List<UserProjectResponseDto> userProjectResponseDtoList = pagedLikeProjectList.content().stream()
            .map(projectLike -> {
            	Project project = projectUseCase.getProjectDetail(projectLike.getProjectGuid());
            	return UserProjectResponseDto.fromDomain(project, null, null);
            })
            .toList();
		return PageResult.of(userProjectResponseDtoList, pagedLikeProjectList.page(), pagedLikeProjectList.size(), pagedLikeProjectList.totalElements());
	}

	public PageResult<UserProjectResponseDto> getUserApplyProjects(String userGuid, PageCommand pageCommand) {
		PageResult<ProjectApplication> pagedApplyProjectList = projectApplicationUseCase.findByApplicantGuid(userGuid, pageCommand);
		List<UserProjectResponseDto> userProjectResponseDtoList = pagedApplyProjectList.content().stream()
            .map(projectApply -> {
            	Project project = projectUseCase.getProjectByRequirementGuid(projectApply.getRequirementGuid());
            	Project projectDetail = projectUseCase.getProjectDetail(project.getProjectGuid());
            	return UserProjectResponseDto.fromDomain(projectDetail, null, projectApply.getStatusCd(), projectApply.getApplicationGuid());
            })
            .toList();
		return PageResult.of(userProjectResponseDtoList, pagedApplyProjectList.page(), pagedApplyProjectList.size(), pagedApplyProjectList.totalElements());
	}

	public void closeProject(String projectGuid, CurrentUserPrincipal authenticatedUser) {
		Project project = projectUseCase.getProjectDetail(projectGuid);
		if(!("ADMIN".equals(authenticatedUser.roleName()) || project.getUserGuid().equals(authenticatedUser.userGuid()))) {
			throw BusinessRuleException.of(ErrorCode.UPDATE_FAIL);
		}
		projectUseCase.closeProject(projectGuid);
	}

	public PageResult<UserProjectResponseDto> getUserParticipateProjects(String userGuid, PageCommand pageCommand) {
		PageResult<Project> pagedParticipateProjectList = projectUseCase.getEndProjectsByApplicantGuid(userGuid, pageCommand);
		List<UserProjectResponseDto> userProjectResponseDtoList = pagedParticipateProjectList.content().stream()
            .map(participateProject -> {
            	List<ProjectApplicationScore> applicationList = projectApplicationUseCase.findAcceptedByProjectGuid(participateProject.getProjectGuid());
            	Project project = projectUseCase.getProjectDetail(participateProject.getProjectGuid());
            	return UserProjectResponseDto.fromDomain(project, applicationList, null);
            })
            .toList();
		return PageResult.of(userProjectResponseDtoList, pagedParticipateProjectList.page(), pagedParticipateProjectList.size(), pagedParticipateProjectList.totalElements());
	}

}
