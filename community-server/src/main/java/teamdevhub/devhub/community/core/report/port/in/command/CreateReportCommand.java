package teamdevhub.devhub.community.core.report.port.in.command;

import lombok.Builder;

@Builder
public record CreateReportCommand(
        String boardGuid,
        String commentGuid,
        String reporterUser,
        String categoryCd,
        String reason
) {
}
