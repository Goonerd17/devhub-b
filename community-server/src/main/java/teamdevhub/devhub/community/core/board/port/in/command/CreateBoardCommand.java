package teamdevhub.devhub.community.core.board.port.in.command;

import lombok.Builder;

@Builder
public record CreateBoardCommand(String title, String categoryCd, String content, String userGuid) {}