package teamdevhub.devhub.project.core.project.domain.vo.command;

import lombok.Builder;

@Builder
public record CreateProjectSkillCommand(String projectGuid, String skillCd) {

}
