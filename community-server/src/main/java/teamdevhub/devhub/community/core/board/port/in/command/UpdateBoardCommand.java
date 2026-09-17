package teamdevhub.devhub.community.core.board.port.in.command;

import lombok.Builder;

@Builder
public record UpdateBoardCommand(String title, String categoryCd, String content, String userGuid, String boardGuid) {}