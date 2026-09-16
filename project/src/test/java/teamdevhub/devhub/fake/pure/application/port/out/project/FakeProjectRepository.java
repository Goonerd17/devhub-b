package teamdevhub.devhub.fake.pure.application.port.out.project;

import java.util.ArrayList;
import java.util.List;

import teamdevhub.devhub.platform.core.common.page.PageCommand;
import teamdevhub.devhub.platform.core.common.page.PageResult;
import teamdevhub.devhub.project.core.project.domain.Project;
import teamdevhub.devhub.project.core.project.domain.vo.command.AdminUpdateProjectCommand;
import teamdevhub.devhub.project.core.project.port.in.command.AdminSearchProjectRequestCommand;
import teamdevhub.devhub.project.core.project.port.in.command.SearchProjectListCommand;
import teamdevhub.devhub.project.core.project.port.out.ProjectRepository;

public class FakeProjectRepository implements ProjectRepository {
	
	private final List<Project> store = new ArrayList<>();

	@Override
	public void save(Project project) {
		store.add(project);
	}

	@Override
	public Project getProjectDetail(String projectGuid) {
		return store.stream()
				.filter(item -> projectGuid.equals(item.getProjectGuid()))
				.findAny()
				.get();
	}

	@Override
	public PageResult<Project> getProjectList(SearchProjectListCommand searchProjectListCommand, PageCommand pageCommand) {
		return null;
	}

	@Override
	public void deleteById(String projectGuid) {

	}

	@Override
	public void update(Project upateProject) {

	}

	@Override
	public PageResult<Project> getUserProjects(String userGuid, PageCommand pageCommand) {
		return null;
	}

	@Override
	public void closeProject(String projectGuid) {

	}

	@Override
	public PageResult<Project> findEndProjectsByApplicantGuid(String userGuid, PageCommand pageCommand) {
		return null;
	}

	@Override
	public Project getProjectByRequirementGuid(String requirementGuid) {
		return null;
	}

	@Override
	public void updateAdminProject(String projectGuid, AdminUpdateProjectCommand adminUpdateProjectCommand) {

	}

	@Override
	public PageResult<Project> getAdminProjectList(AdminSearchProjectRequestCommand adminSearchProjectRequestCommand, PageCommand pageCommand) {
		return null;
	}

}
