package teamdevhub.devhub.community.outbound.board.adapter;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import teamdevhub.devhub.community.core.board.domain.Comment;
import teamdevhub.devhub.community.core.board.port.out.CommentRepository;
import teamdevhub.devhub.community.outbound.board.adapter.entity.CommentEntity;
import teamdevhub.devhub.community.outbound.board.adapter.mapper.CommentMapper;
import teamdevhub.devhub.community.outbound.board.persistence.JpaCommentRepository;
import teamdevhub.devhub.shared.outbound.common.exception.AdapterDataException;
import teamdevhub.devhub.shared.shared.enums.ErrorCode;

@Component
@RequiredArgsConstructor
public class CommentAdapter implements CommentRepository {
	
	private final JpaCommentRepository jpaCommentRepository;
	
	@Override
	public Map<String, Long> countByCommentCount(List<String> boardGuids) {
		return jpaCommentRepository.countByCommentCount(boardGuids)
				.stream()
				.collect(Collectors.toMap(
						row -> (String) row[0],
						row -> (Long) row[1]
						));			
	}
	
	@Override
	public List<Comment> findByBoardGuid(String boardGuid) {
		List<CommentEntity> commentList = jpaCommentRepository.findByBoardGuid(boardGuid);
		return commentList.stream().map(CommentMapper::toComment).toList();
	}

	@Override
	public Comment findByCommentGuid(String commentGuid) {
		CommentEntity commentEntity = jpaCommentRepository.findByCommentGuid(commentGuid)
				.orElseThrow(() -> AdapterDataException.of(ErrorCode.READ_FAIL));
		return CommentMapper.toComment(commentEntity);
	}
	
	@Override
	public void save(Comment comment) {
		jpaCommentRepository.save(CommentMapper.toEntity(comment));
	}
	
	@Override
	public void deleteByBoardGuidAndCommentGuid(String boardGuid, String commentGuid) {
		jpaCommentRepository.deleteByBoardGuidAndCommentGuid(boardGuid, commentGuid);
	}

	@Override
	public void updateComment(Comment comment) {
		jpaCommentRepository.save(CommentMapper.toEntity(comment));
	}

	@Override
	public void deleteByBoardGuids(List<String> boardGuids) {
		jpaCommentRepository.deleteAllByBoardGuidIn(boardGuids);
	}
}
