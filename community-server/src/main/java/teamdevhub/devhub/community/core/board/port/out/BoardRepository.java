package teamdevhub.devhub.community.core.board.port.out;

import java.util.List;

import teamdevhub.devhub.community.core.board.domain.Board;

public interface BoardRepository {

	void save(Board board);

	Board detailBoard(String boardGuid);

	void updateBoard(Board board);

	Board findByBoardGuid(String boardGuid);

	void updateViewCount(String boardGuid);

	void deleteBoard(List<String> boardGuid);
}
