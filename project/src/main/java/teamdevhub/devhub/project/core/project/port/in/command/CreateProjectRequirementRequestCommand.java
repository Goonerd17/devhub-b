package teamdevhub.devhub.project.core.project.port.in.command;

import lombok.Builder;

@Builder
public record CreateProjectRequirementRequestCommand(String position, String level, int capacity) {}
