package teamdevhub.devhub.web.core.project.port.in.facade;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import teamdevhub.devhub.project.core.application.port.in.usecase.ProjectApplicationQueryUseCase;
import teamdevhub.devhub.project.core.application.port.in.usecase.ProjectApplicationUseCase;
import teamdevhub.devhub.platform.security.CurrentUserPrincipal;
import teamdevhub.devhub.platform.core.common.exception.BusinessRuleException;
import teamdevhub.devhub.platform.core.common.page.PageCommand;
import teamdevhub.devhub.platform.core.common.page.PageResult;
import teamdevhub.devhub.media.api.MediaFileMetadataQuery;
import teamdevhub.devhub.administration.api.ApplicationFormDeletion;
import teamdevhub.devhub.project.core.project.domain.Project;
import teamdevhub.devhub.project.core.project.domain.ProjectLike;
import teamdevhub.devhub.project.core.project.domain.vo.command.AdminUpdateProjectCommand;
import teamdevhub.devhub.project.core.project.port.in.command.AdminSearchProjectRequestCommand;
import teamdevhub.devhub.project.core.project.port.in.command.SearchProjectListCommand;
import teamdevhub.devhub.web.core.project.port.in.facade.model.ProjectDetailResponseDto;
import teamdevhub.devhub.project.core.project.port.in.usecase.ProjectApplicationFormUseCase;
import teamdevhub.devhub.project.core.project.port.in.usecase.ProjectLikeUseCase;
import teamdevhub.devhub.project.core.project.port.in.usecase.ProjectUseCase;
import teamdevhub.devhub.member.api.MemberRole;
import teamdevhub.devhub.platform.shared.enums.ErrorCode;

@Service
@RequiredArgsConstructor
public class AdminProjectFacade {
	
	private final ProjectUseCase projectUseCase;
	private final ApplicationFormDeletion applicationFormDeletion;
	private final ProjectApplicationFormUseCase projectApplicationFormUseCase;
	private final MediaFileMetadataQuery mediaFileMetadataQuery;
	private final ProjectLikeUseCase projectLikeUseCase;
	private final ProjectApplicationUseCase projectApplicationUseCase;
	private final ProjectApplicationQueryUseCase projectApplicationQueryUseCase;

	public PageResult<ProjectDetailResponseDto> getProjectList(AdminSearchProjectRequestCommand adminSearchProjectRequestCommand,
			PageCommand pageCommand, CurrentUserPrincipal user) {
		PageResult<Project> pagedProjectList = projectUseCase.getAdminProjectList(adminSearchProjectRequestCommand, pageCommand);
		List<ProjectDetailResponseDto> projectDetailResponseDtoList = new ArrayList<>();
		if(user == null) {
			projectDetailResponseDtoList = pagedProjectList.content().stream()
            .map(project -> {
            	return ProjectDetailResponseDto.fromDomain(project, null, false);
            })
            .toList();
		} else {
	        projectDetailResponseDtoList = pagedProjectList.content().stream()
            .map(project -> {
            	ProjectLike projectLike = projectLikeUseCase.findByProjectGuidAndUserGuid(project.getProjectGuid(), user.userGuid());
            	boolean isProjectLiked = false;
            	if(projectLike != null) isProjectLiked= true;
            	return ProjectDetailResponseDto.fromDomain(project, null, isProjectLiked);
            })
            .toList();
		}
		PageResult<ProjectDetailResponseDto> pagedProjectDetail = PageResult.of(projectDetailResponseDtoList, pagedProjectList.page(), pagedProjectList.size(), pagedProjectList.totalElements());
		
		return pagedProjectDetail;
	}

	public ProjectDetailResponseDto getProjectDetail(String projectGuid, CurrentUserPrincipal user) {
		String imageFileUrl = null;
        Project project = projectUseCase.getProjectDetail(projectGuid);
        if(project.getImageFileGuid() != null && !project.getImageFileGuid().isBlank()) {
        	imageFileUrl = mediaFileMetadataQuery.findMetadata(project.getImageFileGuid()).path();
        }
    	return ProjectDetailResponseDto.fromDomain(project, imageFileUrl, false);
	}

	public void updateProject(String projectGuid, AdminUpdateProjectCommand adminUpdateProjectCommand) {
		projectUseCase.updateAdminProject(projectGuid, adminUpdateProjectCommand);
		
	}

	public void deleteProject(String projectGuid, CurrentUserPrincipal authenticatedUser) {
		Project project = projectUseCase.getProjectDetail(projectGuid);
		if(!(MemberRole.ADMIN.name().equals(authenticatedUser.roleName()) || project.getUserGuid().equals(authenticatedUser.userGuid()))) {
			throw BusinessRuleException.of(ErrorCode.DELETE_FAIL);
		}
		// 프로젝트 지원자 조회 후 지원자가 있으면 return, 지원자 없으면 continue??
		// 삭제해야할 신청폼 목록 반환?
		List<String> deleteApplicationFormGuids =  projectUseCase.deleteProject(projectGuid);
		applicationFormDeletion.deleteApplicationForms(deleteApplicationFormGuids);
	}

}
