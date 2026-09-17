package teamdevhub.devhub.project.core.application.port.out;

import java.util.List;
import java.util.Set;

import teamdevhub.devhub.project.core.application.domain.ProjectApplicationForm;
import teamdevhub.devhub.project.core.application.port.in.command.CreateProjectApplicationFormCommand;

public interface ProjectApplicationFormRepository {

	void saveAll(Set<CreateProjectApplicationFormCommand> forms);

	void deleteByProjectGuid(String projectGuid);

	List<String> findAllGuidByProjectGuid(String projectGuid);

	List<ProjectApplicationForm> findByProjectGuid(String projectGuid);
}
