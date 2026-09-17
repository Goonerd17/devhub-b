package teamdevhub.devhub.community.core.board.port.in.usecase;

import java.util.List;

import teamdevhub.devhub.community.core.board.domain.Board;
import teamdevhub.devhub.community.core.board.port.in.command.CreateBoardCommand;
import teamdevhub.devhub.community.core.board.port.in.command.UpdateBoardCommand;

public interface BoardUseCase {

	void createBoard(CreateBoardCommand createBoardCommand);

	Board detailBoard(String boardGuid, Boolean cookieResult, String userGuid);

	void updateBoard(UpdateBoardCommand updateBoardCommand);

	void deleteBoard(String boardGuid, String userGuid);

	void deleteAdminBoard(List<String> boardGuids);
}