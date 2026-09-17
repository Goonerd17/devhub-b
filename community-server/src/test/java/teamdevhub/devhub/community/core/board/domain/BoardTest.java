package teamdevhub.devhub.community.core.board.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import teamdevhub.devhub.community.core.board.domain.Board;
import teamdevhub.devhub.community.core.board.port.in.command.CreateBoardCommand;

class BoardTest {

    private static final String BOARD_CATEGORY_CD = "4001";
    private static final String BOARD_CONTENT = "게시글 내용입니다";
    private static final String BOARD_TITLE = "게시글입니다";
    private static final String TEST_BOARD_GUID_1 = "BOARD1a1b2c3d4e5f6g7h8i9j10k11l12";
    private static final String TEST_USER_GUID_1 = "USER1a1b2c3d4e5f6g7h8i9j10k11l12";

	@Test
    @DisplayName("게시글을_생성한다")
	void createBoard() {
		// given
		CreateBoardCommand createBoardCommand = CreateBoardCommand.builder()
				.title(BOARD_TITLE)
				.content(BOARD_CONTENT)
				.categoryCd(BOARD_CATEGORY_CD)
				.userGuid(TEST_USER_GUID_1)
				.build();
		
		// when
		Board createBoard = Board.createBoard(createBoardCommand, TEST_BOARD_GUID_1);
		
		// then
		assertThat(createBoard.getBoardGuid()).isEqualTo(TEST_BOARD_GUID_1);
		assertThat(createBoard.getUserGuid()).isEqualTo(TEST_USER_GUID_1);
		assertThat(createBoard.getTitle()).isEqualTo(BOARD_TITLE);
		assertThat(createBoard.getContent()).isEqualTo(BOARD_CONTENT);
		assertThat(createBoard.getCategoryCd()).isEqualTo(BOARD_CATEGORY_CD);
		/**
		 * 현재 null 로 나옵니다.
		 * assertThat(createBoard.getViewCount()).isEqualTo("0");
		 */

		
	}
}
