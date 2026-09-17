package teamdevhub.devhub.community.outbound.board.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import teamdevhub.devhub.community.outbound.board.adapter.entity.CommentEntity;

public interface JpaCommentRepository extends JpaRepository<CommentEntity, String>{
	
	@Query(value = "select c.boardGuid, count(c) " +
					"from CommentEntity c " +
					"where c.boardGuid IN (:boardGuids) " +
					"group by c.boardGuid")
	List<Object[]> countByCommentCount(@Param("boardGuids") List<String> boardGuids);

	List<CommentEntity> findByBoardGuid(String boardGuid);
	Optional<CommentEntity> findByCommentGuid(String commentGuid);

	void deleteByBoardGuidAndCommentGuid(String boardGuid, String commentGuid);

	void deleteAllByBoardGuidIn(List<String> boardGuids);
}
