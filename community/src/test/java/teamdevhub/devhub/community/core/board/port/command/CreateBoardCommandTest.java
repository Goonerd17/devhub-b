package teamdevhub.devhub.community.core.board.port.command;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import teamdevhub.devhub.community.core.board.port.in.command.CreateBoardCommand;

class CreateBoardCommandTest {

    private static final String BOARD_TITLE = "게시글입니다";
    private static final String BOARD_CONTENT = "게시글 내용입니다";
    private static final String BOARD_CATEGORY_CD = "4001";
    private static final String TEST_USER_GUID_1 = "USER1a1b2c3d4e5f6g7h8i9j10k11l12";

	@Test
	@DisplayName("寃뚯떆湲_?앹꽦_而ㅻ㎤??寃利?")
	void createRequestDtoToCommand() {

		// given, when
		CreateBoardCommand createBoardCommand = CreateBoardCommand.builder()
				.userGuid(TEST_USER_GUID_1)
				.title(BOARD_TITLE)
				.content(BOARD_CONTENT)
				.categoryCd(BOARD_CATEGORY_CD)
				.build();
		
		//then
		assertThat(createBoardCommand.userGuid()).isEqualTo(TEST_USER_GUID_1);
		assertThat(createBoardCommand.title()).isEqualTo(BOARD_TITLE);
		assertThat(createBoardCommand.content()).isEqualTo(BOARD_CONTENT);
		assertThat(createBoardCommand.categoryCd()).isEqualTo(BOARD_CATEGORY_CD);
	}

}
