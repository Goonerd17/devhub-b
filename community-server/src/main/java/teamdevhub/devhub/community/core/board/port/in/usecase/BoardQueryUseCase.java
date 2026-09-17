package teamdevhub.devhub.community.core.board.port.in.usecase;

import teamdevhub.devhub.community.core.board.domain.Board;
import teamdevhub.devhub.community.core.board.port.in.command.SearchAdminBoardCommand;
import teamdevhub.devhub.community.core.board.port.in.command.SearchBoardCommand;
import teamdevhub.devhub.shared.core.common.page.PageCommand;
import teamdevhub.devhub.shared.core.common.page.PageResult;

public interface BoardQueryUseCase {

	PageResult<Board> listBoard(SearchBoardCommand searchBoardCommand, PageCommand pageCommand, String userGuid);

	PageResult<Board> listAdminBoard(SearchAdminBoardCommand searchAdminBoardCommand, PageCommand pageCommand);
}
