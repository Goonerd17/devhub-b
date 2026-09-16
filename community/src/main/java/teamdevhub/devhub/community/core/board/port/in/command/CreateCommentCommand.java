package teamdevhub.devhub.community.core.board.port.in.command;

import lombok.Builder;

@Builder
public record CreateCommentCommand(String boardGuid, String content, String userGuid) {}