package teamdevhub.devhub.fake.pure.application.port.in.usecase.board;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import teamdevhub.devhub.community.core.board.domain.Board;
import teamdevhub.devhub.community.core.board.port.in.command.CreateBoardCommand;
import teamdevhub.devhub.community.core.board.port.in.command.UpdateBoardCommand;
import teamdevhub.devhub.community.core.board.port.in.usecase.BoardUseCase;

public class FakeBoardUseCase implements BoardUseCase{

    private static final String TEST_BOARD_GUID_1 = "BOARD1a1b2c3d4e5f6g7h8i9j10k11l12";
	
    private final Map<String, Board> store = new HashMap<>();
    boolean called = false;
    
    @Override
    public void createBoard(CreateBoardCommand createBoardCommand) {
    	this.called = true;
    	Board board = Board.createBoard(createBoardCommand, TEST_BOARD_GUID_1);
    	store.put(board.getBoardGuid(), board);
    }

    @Override
    public Board detailBoard(String boardGuid, Boolean cookieResult, String userGuid) {
        return null;
    }

    @Override
    public void updateBoard(UpdateBoardCommand updateBoardCommand) {

    }

    @Override
    public void deleteBoard(String boardGuid, String requestUserGuid) {

    }

    @Override
    public void deleteAdminBoard(List<String> boardGuids) {

    }

    public boolean isCalled() {
    	return called;
    }
}
