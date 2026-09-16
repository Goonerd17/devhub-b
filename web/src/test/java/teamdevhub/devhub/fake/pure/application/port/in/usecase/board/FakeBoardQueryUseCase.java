package teamdevhub.devhub.fake.pure.application.port.in.usecase.board;

import teamdevhub.devhub.community.core.board.domain.Board;
import teamdevhub.devhub.community.core.board.port.in.command.SearchAdminBoardCommand;
import teamdevhub.devhub.community.core.board.port.in.command.SearchBoardCommand;
import teamdevhub.devhub.community.core.board.port.in.usecase.BoardQueryUseCase;
import teamdevhub.devhub.platform.core.common.page.PageCommand;
import teamdevhub.devhub.platform.core.common.page.PageResult;

public class FakeBoardQueryUseCase implements BoardQueryUseCase {

    @Override
    public PageResult<Board> listBoard(SearchBoardCommand searchBoardCommand, PageCommand pageCommand, String userGuid) {
        return null;
    }

    @Override
    public PageResult<Board> listAdminBoard(SearchAdminBoardCommand searchAdminBoardCommand, PageCommand pageCommand) {
        return null;
    }
}
