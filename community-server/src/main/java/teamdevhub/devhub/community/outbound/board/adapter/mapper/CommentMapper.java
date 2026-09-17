package teamdevhub.devhub.community.outbound.board.adapter.mapper;

import teamdevhub.devhub.community.core.board.domain.Comment;
import teamdevhub.devhub.shared.core.common.audit.AuditInfo;
import teamdevhub.devhub.community.outbound.board.adapter.entity.CommentEntity;

public class CommentMapper {	
	public static Comment toComment(CommentEntity commentEntity) {
		return Comment.builder()
				.commentGuid(commentEntity.getCommentGuid())
				.boardGuid(commentEntity.getBoardGuid())
				.userGuid(commentEntity.getUserGuid())
				.content(commentEntity.getContent())
				.auditInfo(toAuditInfo(commentEntity))
				.build();
	}
	
	private static AuditInfo toAuditInfo(CommentEntity commentEntity) {
		  return AuditInfo.of(
				  commentEntity.getRegistrantGuid(),
				  commentEntity.getRegisteredDate(),
				  commentEntity.getModifierGuid(),
				  commentEntity.getModifiedDate()
	        );
	}

	public static CommentEntity toEntity(Comment comment) {
		return CommentEntity.builder()
				.commentGuid(comment.getCommentGuid())
				.boardGuid(comment.getBoardGuid())
				.userGuid(comment.getUserGuid())
				.content(comment.getContent())
				.build();
	}

}
