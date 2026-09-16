package teamdevhub.devhub.project.core.project.port.out;

import java.util.List;
import java.util.Set;

import teamdevhub.devhub.project.core.project.domain.ProjectRequirement;
import teamdevhub.devhub.project.core.project.domain.vo.command.CreateProjectRequirementCommand;

public interface ProjectRequirementRepository {

	void saveAll(Set<CreateProjectRequirementCommand> positions);

	List<String> selectProjectGuidByPositionCodeAndPositionLevel(List<String> positionCodeList,
			List<String> positionLevelCodeList);

	List<ProjectRequirement> findByProjectGuids(Set<String> projectGuids);

	List<ProjectRequirement> findByProjectGuid(String projectGuid);

	void deleteByProjectGuid(String projectGuid);
	
	ProjectRequirement findById (String projectRequirementGuid);

}
