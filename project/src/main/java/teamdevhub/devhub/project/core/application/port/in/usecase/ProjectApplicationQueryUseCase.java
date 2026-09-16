package teamdevhub.devhub.project.core.application.port.in.usecase;

import teamdevhub.devhub.project.core.application.domain.ProjectApplication;
import teamdevhub.devhub.project.core.application.domain.ProjectApplicationAnswer;
import teamdevhub.devhub.platform.core.common.page.PageCommand;
import teamdevhub.devhub.platform.core.common.page.PageResult;

import java.util.List;
import java.util.Map;

public interface ProjectApplicationQueryUseCase {

	PageResult<ProjectApplication> getApplicationsByProjectGuid(String projectGuid, PageCommand pageCommand);

	ProjectApplication getApplicationByGuid(String applicationGuid);

	List<ProjectApplicationAnswer> getAnswersByApplicationGuid(String applicationGuid);

	Map<String, Long> countApprovedByRequirementGuids(List<String> requirementGuids);
}
