package teamdevhub.devhub.community.core.board.domain;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import teamdevhub.devhub.community.core.board.port.in.command.CreateBoardCommand;
import teamdevhub.devhub.shared.core.common.audit.AuditInfo;


@Getter
@Builder
public class Board {
	
	private final String boardGuid;
	private final String userGuid;
	private String categoryCd;
	private String title;
	private String content;
	private String viewCount;
	
	private String userName;
	private String userFileGuid;
	private String likeCount;
	private String commentCount;

	private List<Comment> commentList;
	private String userEmail;
	private boolean isLiked;
	
	private String userStatus;
	private String reportCount;

	private final AuditInfo auditInfo;

	public static Board of(
			String boardGuid, 
			String userGuid, 
			String categoryCd, 
			String title,
			String content,
			String viewCount, 
			AuditInfo auditInfo
	) {
		return Board.builder()
				.boardGuid(boardGuid)
				.userGuid(userGuid)
				.categoryCd(categoryCd)
				.title(title)
				.content(content)
				.viewCount(viewCount)
				.auditInfo(auditInfo)
                .build();
	}

	public static Board createBoard(CreateBoardCommand createBoardCommand, String boardGuid) {
		return Board.builder()
				.boardGuid(boardGuid)
				.userGuid(createBoardCommand.userGuid())
				.categoryCd(createBoardCommand.categoryCd())
				.title(createBoardCommand.title())
				.content(createBoardCommand.content())
				.build();
	}
	
	public void fillSummarySubquery(String likeCount, String commentCount, String userName, boolean isLiked) {
		 this.likeCount = likeCount;
		 this.commentCount = commentCount;
		 this.userName = userName;
		 this.isLiked = isLiked;
	}
	
	public void fillDetailSubquery(String likeCount, String commentCount, String userName, String userFileGuid, String userEmail, List<Comment> commentList, boolean isLiked) {
		 this.likeCount = likeCount;
		 this.commentCount = commentCount;
		 this.userName = userName;
		 this.userFileGuid = userFileGuid;
		 this.userEmail = userEmail;
		 this.commentList = commentList;
		 this.isLiked = isLiked;
	}

	public void update(String title, String categoryCd, String content) {
		this.title = title;		
		this.categoryCd = categoryCd;	
		this.content = content;	
	}
}
