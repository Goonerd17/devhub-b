package teamdevhub.devhub.project.core.application.port.out;

import teamdevhub.devhub.project.core.application.domain.ProjectApplication;
import teamdevhub.devhub.project.core.application.port.in.command.SearchAdminProjectApplicationCommand;
import teamdevhub.devhub.shared.core.common.page.PageCommand;
import teamdevhub.devhub.shared.core.common.page.PageResult;

public interface AdminProjectApplicationRepository {

	PageResult<ProjectApplication> getApplicationsByProjectGuid(
			SearchAdminProjectApplicationCommand searchAdminProjectApplicationCommand, PageCommand pageCommand);

}
