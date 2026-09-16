package teamdevhub.devhub.project.core.project.domain;

import lombok.Builder;
import lombok.Getter;
import teamdevhub.devhub.project.core.project.domain.vo.command.CreateProjectSkillCommand;

@Builder
@Getter
public class ProjectSkill {
	private String projectSkillGuid;
	private String projectGuid;
	private String skillCd;
	
	public static ProjectSkill createProjectSkill(String projectSkillGuid, CreateProjectSkillCommand createProjectSkillCommand) {
		return ProjectSkill.builder()
				.projectSkillGuid(projectSkillGuid)
				.projectGuid(createProjectSkillCommand.projectGuid())
				.skillCd(createProjectSkillCommand.skillCd())
				.build();
	}
}
