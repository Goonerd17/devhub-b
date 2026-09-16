package teamdevhub.devhub.project.core.application.port.in.usecase;

import java.util.List;

import teamdevhub.devhub.project.core.application.domain.ProjectApplication;
import teamdevhub.devhub.project.core.application.domain.ProjectApplicationAnswer;
import teamdevhub.devhub.project.core.application.domain.ProjectApplicationScore;
import teamdevhub.devhub.project.core.application.port.in.command.ApproveApplicationCommand;
import teamdevhub.devhub.project.core.application.port.in.command.CreateApplicationCommand;
import teamdevhub.devhub.project.core.application.port.in.command.SearchAdminProjectApplicationCommand;
import teamdevhub.devhub.platform.core.common.page.PageCommand;
import teamdevhub.devhub.platform.core.common.page.PageResult;

public interface AdminProjectApplicationUseCase {
	
	PageResult<ProjectApplication> getApplicationsByProjectGuid(SearchAdminProjectApplicationCommand searchAdminProjectApplicationCommand, PageCommand pageCommand);

	ProjectApplication getApplicationByGuid(String applicationGuid);

	List<ProjectApplicationAnswer> getAnswersByApplicationGuid(String applicationGuid);
}
