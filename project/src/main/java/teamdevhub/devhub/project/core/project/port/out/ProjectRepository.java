package teamdevhub.devhub.project.core.project.port.out;


import teamdevhub.devhub.platform.core.common.page.PageCommand;
import teamdevhub.devhub.platform.core.common.page.PageResult;
import teamdevhub.devhub.project.core.project.domain.Project;
import teamdevhub.devhub.project.core.project.domain.vo.command.AdminUpdateProjectCommand;
import teamdevhub.devhub.project.core.project.port.in.command.AdminSearchProjectRequestCommand;
import teamdevhub.devhub.project.core.project.port.in.command.SearchProjectListCommand;

public interface ProjectRepository {

	void save(Project project);
    Project getProjectDetail(String projectGuid);
	PageResult<Project> getProjectList(SearchProjectListCommand searchProjectListCommand, PageCommand pageCommand);
	void deleteById(String projectGuid);
	void update(Project upateProject);
	PageResult<Project> getUserProjects(String userGuid, PageCommand pageCommand);
	void closeProject(String projectGuid);
	PageResult<Project> findEndProjectsByApplicantGuid(String userGuid, PageCommand pageCommand);
	Project getProjectByRequirementGuid(String requirementGuid);
	void updateAdminProject(String projectGuid, AdminUpdateProjectCommand adminUpdateProjectCommand);
	PageResult<Project> getAdminProjectList(AdminSearchProjectRequestCommand adminSearchProjectRequestCommand,
			PageCommand pageCommand);
}
