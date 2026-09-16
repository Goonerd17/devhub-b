package teamdevhub.devhub.project.core.project.application;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import teamdevhub.devhub.project.core.application.port.in.command.CreateProjectApplicationFormCommand;
import teamdevhub.devhub.project.core.application.port.in.usecase.ProjectApplicationQueryUseCase;
import teamdevhub.devhub.project.core.application.port.in.usecase.ProjectApplicationUseCase;
import teamdevhub.devhub.project.core.application.port.out.ProjectApplicationFormRepository;
import teamdevhub.devhub.platform.core.common.page.PageCommand;
import teamdevhub.devhub.platform.core.common.page.PageResult;
import teamdevhub.devhub.platform.identifier.IdentifierProvider;
import teamdevhub.devhub.project.core.project.domain.Project;
import teamdevhub.devhub.project.core.project.domain.ProjectRequirement;
import teamdevhub.devhub.project.core.project.domain.ProjectSkill;
import teamdevhub.devhub.project.core.project.domain.vo.command.AdminUpdateProjectCommand;
import teamdevhub.devhub.project.core.project.domain.vo.command.CreateProjectCommand;
import teamdevhub.devhub.project.core.project.domain.vo.command.CreateProjectRequirementCommand;
import teamdevhub.devhub.project.core.project.domain.vo.command.CreateProjectSkillCommand;
import teamdevhub.devhub.project.core.project.domain.vo.command.UpdateProjectCommand;
import teamdevhub.devhub.project.core.project.port.in.command.AdminSearchProjectRequestCommand;
import teamdevhub.devhub.project.core.project.port.in.command.CreateProjectRequirementRequestCommand;
import teamdevhub.devhub.project.core.project.port.in.command.SearchProjectListCommand;
import teamdevhub.devhub.project.core.project.port.in.usecase.ProjectUseCase;
import teamdevhub.devhub.project.core.project.port.out.ProjectLikeRepository;
import teamdevhub.devhub.project.core.project.port.out.ProjectRepository;
import teamdevhub.devhub.project.core.project.port.out.ProjectRequirementRepository;
import teamdevhub.devhub.project.core.project.port.out.ProjectSkillRepository;
import teamdevhub.devhub.project.api.AdminMemberProjectQuery;
import teamdevhub.devhub.project.api.AdminMemberProjectResult;
import teamdevhub.devhub.project.api.AdminMemberProjectPage;
import teamdevhub.devhub.project.outbound.project.adapter.mapper.ProjectMapper;

@Service
@Transactional
@RequiredArgsConstructor
public class ProjectService implements ProjectUseCase, AdminMemberProjectQuery {

	private final IdentifierProvider identifierProvider;
	
	private final ProjectRepository projectRepository;
	private final ProjectSkillRepository projectSkillRepository;
	private final ProjectRequirementRepository projectRequirementRepository;
	private final ProjectApplicationFormRepository projectApplicationFormRepository;
	private final ProjectLikeRepository projectLikeRepository;
	private final ProjectApplicationQueryUseCase projectApplicationQueryUseCase;
	private final ProjectApplicationUseCase projectApplicationUseCase;

	@Override
	public AdminMemberProjectPage findRegisteredProjects(String userGuid, int page, int size) {
		var result=getUserProjects(userGuid, new PageCommand(page, size));
		return toPage(result, result.content().stream().map(this::toAdminResult).toList());
	}

	@Override
	public AdminMemberProjectPage findAppliedProjects(String userGuid, int page, int size) {
		var result=projectApplicationUseCase.findByApplicantGuid(userGuid, new PageCommand(page, size));
		return toPage(result, result.content().stream()
				.map(a -> toAdminResult(getProjectByRequirementGuid(a.getRequirementGuid()), a.getApplicationGuid(), a.getStatusCd())).toList());
	}
	private AdminMemberProjectPage toPage(PageResult<?> p,List<AdminMemberProjectResult> c){return new AdminMemberProjectPage(c,p.page(),p.size(),p.totalElements(),p.totalPages(),p.first(),p.last());}

	private AdminMemberProjectResult toAdminResult(Project p) { return toAdminResult(p, null, null); }
	private AdminMemberProjectResult toAdminResult(Project p, String applicationGuid, String approvalState) {
		var apps = projectApplicationQueryUseCase.getApplicationsByProjectGuid(p.getProjectGuid(), new PageCommand(0, Integer.MAX_VALUE)).content().stream()
				.map(a -> new AdminMemberProjectResult.Application(a.getApplicationGuid(), a.getApplicantGuid(), a.getUserName(), a.getEmail(), a.getStatusCd(), a.getMannerDegree(), 0.0)).toList();
		return new AdminMemberProjectResult(p.getProjectGuid(), p.getUserGuid(), p.getUsername(), p.getCategory(), p.getTitle(), p.getContent(), p.getAttachmentFileGuid(), p.getImageFileGuid(), p.getRecruitmentTypeCd(), p.getRecruitmentStartDate(), p.getRecruitmentEndDate(), p.getProgressTypeCd(), p.getProgressRegionCd(), p.getProgressPeriod(), p.getProgressStartDate(), p.getProgressEndDate(), p.getRecruitStatus(), String.valueOf(apps.stream().filter(a -> "APPROVED".equals(a.statusCd())).count()), "0", String.valueOf(apps.size()), "0", approvalState, p.isProgressCompleted() ? "end" : "ing", applicationGuid, p.getAuditInfo() == null ? null : p.getAuditInfo().registeredDate(), p.getAuditInfo() == null ? null : p.getAuditInfo().modifiedDate(), apps);
	}
	
	@Override
	public void createProject(CreateProjectCommand createProjectCommand, String username) {
		Project project = createGeneralProject(createProjectCommand, username);
		saveProjectSkills(project.getProjectGuid(), createProjectCommand.skillList());
		saveProjectRequirement(project.getProjectGuid(), createProjectCommand.positionList());
		saveProjectApplicationForm(project.getProjectGuid(), createProjectCommand.applicationFormList());
		projectRepository.save(project);
	}

	private Project createGeneralProject(CreateProjectCommand createProjectCommand, String username) {
		String projectGuid = identifierProvider.generateIdentifier();
		return Project.createProject(createProjectCommand, projectGuid, username);
		
	}
	
	private void saveProjectSkills(String projectGuid, List<String> skillList) {
		Set<CreateProjectSkillCommand> skills = skillList.stream()
				.map(skill -> new CreateProjectSkillCommand(projectGuid, skill))
				.collect(Collectors.toUnmodifiableSet());
		projectSkillRepository.saveAll(skills);
	}

	private void saveProjectRequirement(String projectGuid, List<CreateProjectRequirementRequestCommand> positionList) {
		Set<CreateProjectRequirementCommand> positions = positionList.stream()
				.map(position -> new CreateProjectRequirementCommand(projectGuid, position.position(), position.level(), position.capacity()))
				.collect(Collectors.toUnmodifiableSet());
		projectRequirementRepository.saveAll(positions);
	}
	

	private void saveProjectApplicationForm(String projectGuid, List<String> applicationFormList) {
		Set<CreateProjectApplicationFormCommand> forms = applicationFormList.stream()
				.map(form -> new CreateProjectApplicationFormCommand(projectGuid, form))
				.collect(Collectors.toUnmodifiableSet());
		projectApplicationFormRepository.saveAll(forms);
		
	}

	@Override
	public PageResult<Project> getProjectList(SearchProjectListCommand searchProjectListCommand,
			PageCommand pageCommand) {
		PageResult<Project> pagedProjectList = projectRepository.getProjectList(searchProjectListCommand, pageCommand);
		Set<String> projectGuids = pagedProjectList.content().stream()
		        .map(Project::getProjectGuid)
		        .collect(Collectors.toSet());
		Map<String, List<String>> mapSKill = ProjectMapper.toMapSkill(projectSkillRepository.findByProjectGuids(projectGuids));
		Map<String, List<ProjectRequirement>> mapRequirement = ProjectMapper.toMapRequirement(projectRequirementRepository.findByProjectGuids(projectGuids));
		Map<String, String> mapLikeCount = ProjectMapper.toMapLikeCount(projectLikeRepository.findByProjectGuid(projectGuids));
		
		List<Project> content = pagedProjectList.content().stream()
				.map(project -> ProjectMapper.toProjectDetail(
						project,
						mapSKill.getOrDefault(project.getProjectGuid(), List.of()),
						mapRequirement.getOrDefault(project.getProjectGuid(), List.of()),
						 mapLikeCount.getOrDefault(project.getProjectGuid(), String.valueOf(0))))
				.toList();
		return PageResult.of(
				content,
        		pagedProjectList.page(),
        		pagedProjectList.size(),
        		pagedProjectList.totalElements());
    }

	@Override
	public Project getProjectDetail(String projectGuid) {
		Project project = projectRepository.getProjectDetail(projectGuid);
		List<String> skillList = projectSkillRepository.findByProjectGuid(projectGuid).stream()
									.map(ProjectSkill::getSkillCd)
									.toList();
		List<ProjectRequirement> requirementList = projectRequirementRepository.findByProjectGuid(projectGuid);
		int likeCount = projectLikeRepository.countByProjectGuid(projectGuid);
		
		return ProjectMapper.toProject(project, skillList, requirementList, String.valueOf(likeCount));		
	}

	@Override
	public List<String> deleteProject(String projectGuid) {
		projectSkillRepository.deleteByProjectGuid(projectGuid);
		projectRequirementRepository.deleteByProjectGuid(projectGuid);
		projectApplicationFormRepository.deleteByProjectGuid(projectGuid);
		projectLikeRepository.deleteByProjectGuid(projectGuid);
		projectRepository.deleteById(projectGuid);
		return projectApplicationFormRepository.findAllGuidByProjectGuid(projectGuid);
	}

	@Override
	public List<String> updateProject(String projectGuid, UpdateProjectCommand updateProjectCommand) {
		List<String> deleteAppplicationFormGuids = projectApplicationFormRepository.findAllGuidByProjectGuid(projectGuid);
		projectSkillRepository.deleteByProjectGuid(projectGuid);
		projectRequirementRepository.deleteByProjectGuid(projectGuid);
		projectApplicationFormRepository.deleteByProjectGuid(projectGuid);
		// 신청양식 변경되면 어떻게???
		// 일단 삭제 후 생성
		saveProjectSkills(projectGuid, updateProjectCommand.skillList());
		saveProjectRequirement(projectGuid, updateProjectCommand.positionList());
		saveProjectApplicationForm(projectGuid, updateProjectCommand.applicationFormList());
		projectRepository.update(Project.createUpateProject(updateProjectCommand, projectGuid));
		return deleteAppplicationFormGuids;
	}

	@Override
	public PageResult<Project> getUserProjects(String userGuid, PageCommand pageCommand) {
		return projectRepository.getUserProjects(userGuid, pageCommand);
	}

	@Override
	public void closeProject(String projectGuid) {
		projectRepository.closeProject(projectGuid);
		
	}

	@Override
	public PageResult<Project> getEndProjectsByApplicantGuid(String userGuid, PageCommand pageCommand) {
		return projectRepository.findEndProjectsByApplicantGuid(userGuid, pageCommand);
	}

	@Override
	public Project getProjectByRequirementGuid(String requirementGuid) {
		return projectRepository.getProjectByRequirementGuid(requirementGuid);
	}

	@Override
	public void updateAdminProject(String projectGuid, AdminUpdateProjectCommand adminUpdateProjectCommand) {
		projectRepository.updateAdminProject(projectGuid, adminUpdateProjectCommand);
		
	}

	@Override
	public PageResult<Project> getAdminProjectList(AdminSearchProjectRequestCommand adminSearchProjectRequestCommand,
			PageCommand pageCommand) {
		PageResult<Project> pagedProjectList = projectRepository.getAdminProjectList(adminSearchProjectRequestCommand, pageCommand);
		
		return pagedProjectList;
	}

}
