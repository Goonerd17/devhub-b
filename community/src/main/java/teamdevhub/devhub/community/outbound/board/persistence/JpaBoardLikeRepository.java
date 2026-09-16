package teamdevhub.devhub.community.outbound.board.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import teamdevhub.devhub.community.outbound.board.adapter.entity.BoardLikeEntity;

public interface JpaBoardLikeRepository extends JpaRepository<BoardLikeEntity, String>{
	
	@Query(value = "select bl.boardGuid, count(bl) " +
					"from BoardLikeEntity bl " +
					"where bl.boardGuid IN (:boardGuids) " +
					"group by bl.boardGuid")
	List<Object[]> countByLikeCount(@Param("boardGuids") List<String> boardGuids);

	Optional<BoardLikeEntity> findByBoardGuidAndUserGuid(String boardGuid, String userGuid);

	boolean existsByBoardGuidAndUserGuid(String boardGuid, String userGuid);

	@Query("select bl.boardGuid " +
					"from BoardLikeEntity bl " +
					"where bl.userGuid = :userGuid and bl.boardGuid IN (:boardGuids)")
	List<String> findLikedBoardGuids(@Param("userGuid") String userGuid, @Param("boardGuids") List<String> boardGuids);

	void deleteAllByBoardGuidIn(List<String> boardGuids);
}
