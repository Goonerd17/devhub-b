package teamdevhub.devhub.project.core.application.port.in.usecase;

import java.util.List;

import teamdevhub.devhub.project.core.application.domain.ProjectApplication;
import teamdevhub.devhub.project.core.application.domain.ProjectApplicationScore;
import teamdevhub.devhub.project.core.application.port.in.command.ApproveApplicationCommand;
import teamdevhub.devhub.project.core.application.port.in.command.CreateApplicationCommand;
import teamdevhub.devhub.shared.core.common.page.PageCommand;
import teamdevhub.devhub.shared.core.common.page.PageResult;

public interface ProjectApplicationUseCase {

	void createApplication(CreateApplicationCommand command);

	void approveApplication(ApproveApplicationCommand command);

	void cancelApplication(String applicationGuid, String applicantGuid);

	PageResult<ProjectApplication> findByApplicantGuid(String userGuid, PageCommand pageCommand);

	List<ProjectApplicationScore> findAcceptedByProjectGuid(String projectGuid);
}
