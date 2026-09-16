package teamdevhub.devhub.community.core.board.port.out;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import teamdevhub.devhub.community.core.board.domain.BoardLike;

public interface BoardLikeRepository {

	Map<String, Long> countByLikeCount(List<String> boardGuids);

	Optional<BoardLike> likeBoard(String boardGuid, String userGuid);

	void deleteBoardLike(BoardLike boardLike);

	void save(BoardLike boardLike);

	boolean existsByBoardGuidAndUserGuid(String boardGuid, String userGuid);

	List<String> findLikedBoardGuids(String userGuid, List<String> boardGuids);

	void deleteByBoardGuids(List<String> boardGuids);

}
