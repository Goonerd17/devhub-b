package teamdevhub.devhub.project.outbound.project.adapter;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import teamdevhub.devhub.platform.core.common.page.PageCommand;
import teamdevhub.devhub.platform.core.common.page.PageResult;
import teamdevhub.devhub.project.core.project.domain.Project;
import teamdevhub.devhub.project.core.project.domain.vo.command.AdminUpdateProjectCommand;
import teamdevhub.devhub.project.core.project.port.in.command.AdminSearchProjectRequestCommand;
import teamdevhub.devhub.project.core.project.port.in.command.SearchProjectListCommand;
import teamdevhub.devhub.project.core.project.port.out.ProjectRepository;
import teamdevhub.devhub.project.outbound.project.adapter.entity.ProjectEntity;
import teamdevhub.devhub.project.outbound.project.adapter.mapper.ProjectMapper;
import teamdevhub.devhub.project.outbound.project.persistence.JpaProjectRepository;

@Component
@RequiredArgsConstructor
public class ProjectAdapter implements ProjectRepository {
	
	private final JpaProjectRepository jpaProjectRepository;

	@Override
	public void save(Project project) {
		jpaProjectRepository.save(ProjectMapper.toEntity(project));
	}
	
	@Override
	public Project getProjectDetail(String projectGuid) {
        ProjectEntity entity = jpaProjectRepository.findById(projectGuid).orElseThrow();
        return ProjectMapper.toProject(entity);
	}
	
	@Override
    public PageResult<Project> getProjectList(SearchProjectListCommand searchProjectListCommand, PageCommand pageCommand) {
        Pageable pageable = PageRequest.of(pageCommand.page(), pageCommand.size());

        Page<ProjectEntity> pagedProjectList= jpaProjectRepository.findBySearchCondition(
        		searchProjectListCommand.keyword(), searchProjectListCommand.order(),
				searchProjectListCommand.skillCodeList(), searchProjectListCommand.regionCodeList(), searchProjectListCommand.positionCodeList(),
				searchProjectListCommand.positionLevelCodeList(), searchProjectListCommand.projectRecruitTypeList(),
				searchProjectListCommand.projectRecruitStatusList(), searchProjectListCommand.projectProgressTypeList(),
				searchProjectListCommand.recruitmentStartDate(), searchProjectListCommand.recruitmentEndDate(),
				searchProjectListCommand.progressStartDate(), searchProjectListCommand.progressEndDate(), searchProjectListCommand.progressPeriodList(), pageable);
        
        return PageResult.of(
        		pagedProjectList.getContent().stream().map(ProjectMapper::toProject).toList(),
        		pagedProjectList.getNumber(),
        		pagedProjectList.getSize(),
        		pagedProjectList.getTotalElements());
    }

	@Override
	public void deleteById(String projectGuid) {
		jpaProjectRepository.deleteById(projectGuid);
	}

	@Override
	public void update(Project upateProject) {
		jpaProjectRepository.update(upateProject.getProjectGuid(), upateProject.getUserGuid(), upateProject.getUsername(), upateProject.getAttachmentFileGuid(),
			upateProject.getImageFileGuid(), upateProject.getTitle(), upateProject.getContent(), upateProject.getRecruitmentStartDate(), upateProject.getRecruitmentEndDate(),
			upateProject.getProgressStartDate(), upateProject.getProgressEndDate(), upateProject.getRecruitmentTypeCd(), upateProject.getProgressRegionCd(), 
			upateProject.getProgressTypeCd(), upateProject.getCategory());
	}

	@Override
	public PageResult<Project> getUserProjects(String userGuid, PageCommand pageCommand) {
		Pageable pageable = PageRequest.of(pageCommand.page(), pageCommand.size());

        Page<ProjectEntity> pagedProjectList= jpaProjectRepository.findByUserGuid(userGuid, pageable);
        return PageResult.of(
        		pagedProjectList.getContent().stream().map(ProjectMapper::toProject).toList(),
        		pagedProjectList.getNumber(),
        		pagedProjectList.getSize(),
        		pagedProjectList.getTotalElements());
    }

	@Override
	public void closeProject(String projectGuid) {
		jpaProjectRepository.closeProject(projectGuid);
	}

	@Override
	public PageResult<Project> findEndProjectsByApplicantGuid(String userGuid, PageCommand pageCommand) {
		Pageable pageable = PageRequest.of(pageCommand.page(), pageCommand.size());
		
		Page<ProjectEntity> pagedProjectList= jpaProjectRepository.findEndProjectsByApplicantGuid(userGuid, pageable);
		return PageResult.of(
        		pagedProjectList.getContent().stream().map(ProjectMapper::toProject).toList(),
        		pagedProjectList.getNumber(),
        		pagedProjectList.getSize(),
        		pagedProjectList.getTotalElements());
	}

	@Override
	public Project getProjectByRequirementGuid(String requirementGuid) {
		ProjectEntity entity = jpaProjectRepository.getProjectByRequirementGuid(requirementGuid);
		return ProjectMapper.toProject(entity);
	}

	@Override
	public void updateAdminProject(String projectGuid, AdminUpdateProjectCommand adminUpdateProjectCommand) {
		jpaProjectRepository.updateAdminProject(projectGuid, adminUpdateProjectCommand.title(), adminUpdateProjectCommand.recruitmentTypeCd(),
				adminUpdateProjectCommand.progressTypeCd(), adminUpdateProjectCommand.progressRegionCd(),
				adminUpdateProjectCommand.recruitmentStartDate(), adminUpdateProjectCommand.recruitmentEndDate(),
				adminUpdateProjectCommand.progressStartDate(), adminUpdateProjectCommand.progressEndDate());
		
	}

	@Override
	public PageResult<Project> getAdminProjectList(AdminSearchProjectRequestCommand adminSearchProjectRequestCommand,
			PageCommand pageCommand) {
		Pageable pageable = PageRequest.of(pageCommand.page(), pageCommand.size());

        Page<ProjectEntity> pagedProjectList= jpaProjectRepository.findByAdminSearchCondition(
        		adminSearchProjectRequestCommand.keyword(), adminSearchProjectRequestCommand.recruitmentTypeCd(), adminSearchProjectRequestCommand.recruitStatusCd(),
        		adminSearchProjectRequestCommand.progressTypeCd(), adminSearchProjectRequestCommand.progressRegionCd(),
				adminSearchProjectRequestCommand.recruitmentStartDate(), adminSearchProjectRequestCommand.recruitmentEndDate(),
				adminSearchProjectRequestCommand.progressStartDate(), adminSearchProjectRequestCommand.progressEndDate(), pageable);
        
        return PageResult.of(
        		pagedProjectList.getContent().stream().map(ProjectMapper::toProject).toList(),
        		pagedProjectList.getNumber(),
        		pagedProjectList.getSize(),
        		pagedProjectList.getTotalElements());
	}

}
