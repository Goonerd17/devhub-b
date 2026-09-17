package teamdevhub.devhub.project.core.project.port.out;

import java.util.List;
import java.util.Set;

import teamdevhub.devhub.shared.core.common.page.PageCommand;
import teamdevhub.devhub.shared.core.common.page.PageResult;
import teamdevhub.devhub.project.core.project.domain.ProjectLike;

public interface ProjectLikeRepository {

	List<ProjectLike> findByProjectGuid(Set<String> projectGuids);

	int countByProjectGuid(String projectGuid);

	void deleteByProjectGuid(String projectGuid);

	void save(ProjectLike projectLike);

	ProjectLike findByProjectGuidAndUserGuid(String projectGuid, String userGuid);

	void deleteById(String projectLikeGuid);

	PageResult<ProjectLike> findByUserGuid(String userGuid, PageCommand pageCommand);

}
