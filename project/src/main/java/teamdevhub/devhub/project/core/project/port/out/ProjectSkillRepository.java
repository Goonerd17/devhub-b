package teamdevhub.devhub.project.core.project.port.out;

import java.util.List;
import java.util.Set;

import teamdevhub.devhub.project.core.project.domain.ProjectSkill;
import teamdevhub.devhub.project.core.project.domain.vo.command.CreateProjectSkillCommand;

public interface ProjectSkillRepository {

	void saveAll(Set<CreateProjectSkillCommand> skills);

	List<String> selectProjectGuidBySkillCd(List<String> skillCodeList);

	List<ProjectSkill> findByProjectGuids(Set<String> projectGuids);

	List<ProjectSkill> findByProjectGuid(String projectGuid);

	void deleteByProjectGuid(String projectGuid);

}
