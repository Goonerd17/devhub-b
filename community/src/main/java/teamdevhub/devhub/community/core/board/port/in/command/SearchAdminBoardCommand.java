package teamdevhub.devhub.community.core.board.port.in.command;

import java.time.LocalDateTime;

import lombok.Builder;

@Builder
public record SearchAdminBoardCommand(
		LocalDateTime registeredStartDate,
		LocalDateTime registeredEndDate,
        String title,
        String categoryCd,
        String userStatus,
        Boolean isReported
) {
}