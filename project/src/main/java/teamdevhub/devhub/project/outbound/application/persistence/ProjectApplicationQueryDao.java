package teamdevhub.devhub.project.outbound.application.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import teamdevhub.devhub.project.core.application.domain.ProjectApplication;
import teamdevhub.devhub.project.core.application.domain.ProjectApplicationAnswer;
import teamdevhub.devhub.project.core.application.domain.ProjectApplicationScore;

import java.util.List;

public interface ProjectApplicationQueryDao {

	Page<ProjectApplication> findApplicationsByProjectGuid(String projectGuid, Pageable pageable);

	ProjectApplication findApplicationByGuid(String applicationGuid);

	List<ProjectApplicationAnswer> findAnswersByApplicationGuid(String applicationGuid);

	List<ProjectApplicationScore> findAcceptedByProjectGuid(String projectGuid);
}
