package teamdevhub.devhub.community.core.board.port.in.command;

import lombok.Builder;

@Builder
public record UpdateCommentCommand(String boardGuid, String content, String userGuid, String commentGuid) {}