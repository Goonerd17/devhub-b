package teamdevhub.devhub.community.core.board.port.out;

import java.util.List;
import java.util.Map;

import teamdevhub.devhub.community.core.board.domain.Comment;

public interface CommentRepository {

	Map<String, Long> countByCommentCount(List<String> boardGuids);

	List<Comment> findByBoardGuid(String boardGuid);
	
	Comment findByCommentGuid(String commentGuid);

	void save(Comment comment);

	void deleteByBoardGuidAndCommentGuid(String boardGuid, String commentGuid);

	void deleteByBoardGuids(List<String> boardGuids);

	void updateComment(Comment comment);
}
