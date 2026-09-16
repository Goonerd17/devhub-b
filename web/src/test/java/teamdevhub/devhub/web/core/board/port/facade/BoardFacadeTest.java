package teamdevhub.devhub.web.core.board.port.facade;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import teamdevhub.devhub.web.api.board.facade.BoardFacade;
import teamdevhub.devhub.community.core.board.port.in.command.CreateBoardCommand;
import teamdevhub.devhub.fake.pure.application.port.in.usecase.board.FakeBoardLikeUseCase;
import teamdevhub.devhub.fake.pure.application.port.in.usecase.board.FakeBoardQueryUseCase;
import teamdevhub.devhub.fake.pure.application.port.in.usecase.board.FakeBoardUseCase;

public class BoardFacadeTest {

    private static final String BOARD_CATEGORY_CD = "4001";
    private static final String BOARD_CONTENT = "게시글 내용입니다";
    private static final String BOARD_TITLE = "게시글입니다";
    private static final String TEST_USER_GUID_1 = "USER1a1b2c3d4e5f6g7h8i9j10k11l12";

	private BoardFacade boardFacade;

	private FakeBoardUseCase boardUseCase;
	private FakeBoardQueryUseCase boardQueryUseCase;
	private FakeBoardLikeUseCase boardLikeUseCase;

	@BeforeEach
	void init() {
		boardUseCase = new FakeBoardUseCase();
		boardLikeUseCase = new FakeBoardLikeUseCase();

		boardFacade = new BoardFacade(boardQueryUseCase, boardUseCase, boardLikeUseCase);
	}
	
	@Test
	@DisplayName("게시글_생성_요청이_전달된다")
	void createBoard() {
		// given
		CreateBoardCommand createBoardCommand = new CreateBoardCommand(
				BOARD_TITLE,
				BOARD_CONTENT,
				BOARD_CATEGORY_CD,
				TEST_USER_GUID_1
		);
		
		// when
		boardFacade.createBoard(createBoardCommand);
		
		// then
		assertThat(boardUseCase.isCalled()).isTrue();
	}
}
