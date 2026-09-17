package teamdevhub.devhub.community.core.board.domain;


import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BoardLike {
	
	private final String boardLikeGuid;
	private final String boardGuid;
	private final String userGuid;
	
	
	public static BoardLike of(
			String boardLikeGuid, 
			String boardGuid, 
			String userGuid
	) {
		return BoardLike.builder()
				.boardLikeGuid(boardLikeGuid)
				.boardGuid(boardGuid)
				.userGuid(userGuid)
				.build();
	}


	public static BoardLike createBoardLike(String boardGuid, String userGuid, String boardLikeGuid) {
		return BoardLike.builder()
				.boardLikeGuid(boardLikeGuid)
				.boardGuid(boardGuid)
				.userGuid(userGuid)
				.build();
	}
}
