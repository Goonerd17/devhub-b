package teamdevhub.devhub.community.outbound.board.adapter.mapper;

import teamdevhub.devhub.community.core.board.domain.BoardLike;
import teamdevhub.devhub.community.outbound.board.adapter.entity.BoardLikeEntity;

public class BoardLikeMapper {
	
	public static BoardLikeEntity toEntity(BoardLike boardLike) {
		return BoardLikeEntity.builder()
				.boardLikeGuid(boardLike.getBoardLikeGuid())
				.boardGuid(boardLike.getBoardGuid())
				.userGuid(boardLike.getUserGuid())
				.build();
	}
	
	
	public static BoardLike toDomain(BoardLikeEntity boardLikeEntity) {
		return BoardLike.of(
				boardLikeEntity.getBoardLikeGuid(),
				boardLikeEntity.getBoardGuid(),
				boardLikeEntity.getUserGuid()
		);
	}

}
