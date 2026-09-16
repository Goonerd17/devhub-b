package teamdevhub.devhub.community.core.board.domain;


import lombok.Builder;
import lombok.Getter;
import teamdevhub.devhub.community.core.board.port.in.command.CreateCommentCommand;
import teamdevhub.devhub.platform.core.common.audit.AuditInfo;


@Getter
@Builder
public class Comment {
	
	private final String commentGuid;
	private final String boardGuid;
	private final String userGuid;
	private String content;
	
	private String userName;
	
	private final AuditInfo auditInfo;
	
	public void fillUserName(String userName) {
		 this.userName = userName;
	}

	public void updateContent(String content) {
		this.content = content;
	}

	public static Comment createComment(CreateCommentCommand createCommentCommand, String commentGuid) {
		return Comment.builder()
				.commentGuid(commentGuid)
				.boardGuid(createCommentCommand.boardGuid())
				.userGuid(createCommentCommand.userGuid())
				.content(createCommentCommand.content())
				.build();
	}
}
