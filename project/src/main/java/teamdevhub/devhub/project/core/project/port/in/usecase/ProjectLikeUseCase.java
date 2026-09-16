package teamdevhub.devhub.project.core.project.port.in.usecase;

import teamdevhub.devhub.platform.core.common.page.PageCommand;
import teamdevhub.devhub.platform.core.common.page.PageResult;
import teamdevhub.devhub.project.core.project.domain.ProjectLike;
import teamdevhub.devhub.project.core.project.domain.vo.command.CreateProjectLikeCommand;

public interface ProjectLikeUseCase {

	void toggleProjectLike(CreateProjectLikeCommand createProjectLikeCommand);

	ProjectLike findByProjectGuidAndUserGuid(String projectGuid, String userGuid);

	PageResult<ProjectLike> findByUserGuid(String userGuid, PageCommand pageCommand);

}
