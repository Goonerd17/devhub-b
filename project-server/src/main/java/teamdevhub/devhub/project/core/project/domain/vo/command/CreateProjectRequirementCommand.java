package teamdevhub.devhub.project.core.project.domain.vo.command;

import lombok.Builder;

@Builder
public record CreateProjectRequirementCommand(String projectGuid,  String position, String level, int capacity) {

}
