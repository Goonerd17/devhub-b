package teamdevhub.devhub.project.core.project.port.in.usecase;

import teamdevhub.devhub.platform.core.common.page.PageCommand;
import teamdevhub.devhub.platform.core.common.page.PageResult;
import teamdevhub.devhub.project.core.project.domain.Project;
import teamdevhub.devhub.project.core.project.port.in.command.SearchProjectListCommand;

public interface ProjectQueryUseCase {

    PageResult<Project> getProjectList(SearchProjectListCommand searchProjectListCommand, PageCommand pageCommand);
}

