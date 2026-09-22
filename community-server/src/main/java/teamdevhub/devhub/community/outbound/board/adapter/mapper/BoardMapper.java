package teamdevhub.devhub.community.outbound.board.adapter.mapper;

import teamdevhub.devhub.community.core.board.domain.Board;
import teamdevhub.devhub.shared.core.common.audit.AuditInfo;
import teamdevhub.devhub.community.outbound.board.adapter.entity.BoardEntity;

public class BoardMapper {
	public static BoardEntity toEntity(Board board) {
		return BoardEntity.builder()
				.boardGuid(board.getBoardGuid())
				.userGuid(board.getUserGuid())
				.categoryCd(board.getCategoryCd())
				.title(board.getTitle())
				.content(board.getContent())
				.build();
	}
	
	public static Board toDomain(BoardEntity boardEntity) {
		return Board.of(
				boardEntity.getBoardGuid(),
				boardEntity.getUserGuid(),
				boardEntity.getCategoryCd(),
				boardEntity.getTitle(),
				boardEntity.getContent(),
				String.valueOf(boardEntity.getViewCount()),
				toAuditInfo(boardEntity)
		);
	}
	
	private static AuditInfo toAuditInfo(BoardEntity boardEntity) {
		  return AuditInfo.of(
				  boardEntity.getRegistrantGuid(),
				  boardEntity.getRegisteredDate(),
				  boardEntity.getModifierGuid(),
				  boardEntity.getModifiedDate()
	        );
	}
	
	public static Board toBoard(BoardEntity boardEntity) {
		return Board.builder()
				.boardGuid(boardEntity.getBoardGuid())
				.userGuid(boardEntity.getUserGuid())
				.categoryCd(boardEntity.getCategoryCd())
				.title(boardEntity.getTitle())
				.content(boardEntity.getContent())
				.viewCount(String.valueOf(boardEntity.getViewCount()))
				.auditInfo(toAuditInfo(boardEntity))
				.build();
	}

	public static Board toAdminBoard(BoardEntity boardEntity, String username, boolean deleted, boolean blocked, Long reportCount) {
		return Board.builder()
				.boardGuid(boardEntity.getBoardGuid())
				.title(boardEntity.getTitle())
				.categoryCd(boardEntity.getCategoryCd())
				.userName(username)
				.userStatus(userStatusCode(deleted, blocked))
				.reportCount(String.valueOf(reportCount))
				.auditInfo(toAuditInfo(boardEntity))
				.build();
	}

	private static String userStatusCode(boolean deleted, boolean blocked) {
		if (deleted) return "7002";
		if (blocked) return "7003";
		return "7001";
	}
	
	

}
