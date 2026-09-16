//package teamdevhub.devhub.community.core.board.application.service;
//
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//
//import teamdevhub.devhub.constant.UserTestConstant;
//import teamdevhub.devhub.community.core.board.application.BoardService;
//import teamdevhub.devhub.community.core.board.port.in.command.CreateBoardCommand;
//import teamdevhub.devhub.fake.pure.application.port.out.board.FakeBoardRepository;
//import teamdevhub.devhub.fake.pure.application.provider.FakeUuidIdentifierProvider;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static teamdevhub.devhub.constant.BoardTestConstant.*;
//
//public class BoardServiceTest {
//
//	private BoardService boardService;
//	private FakeBoardRepository boardRepository;
//
//	@BeforeEach
//	void init() {
//		boardRepository = new FakeBoardRepository();
//        FakeUuidIdentifierProvider fakeUuidIdentifierProvider = new FakeUuidIdentifierProvider(TEST_BOARD_GUID_1);
//
//		/**
//		 * 리뷰
//		 * 게시글 서비스 생성자 의존성 수정 필요
//		 * @BeforeEach는 테스트 소스 실행 전 독립적으로 모두 실행되는 소스입니다.
//		 * 따라서 아래 createBoard() 테스트 코드에서 새롭게 boardService 를 선언할 필요없이, 기존 boardService 를 사용해도 됩니다.
//		 */
//		boardService = new BoardService(
//				fakeUuidIdentifierProvider,
//				boardRepository
//		);
//	}
//
//	@Test
//	@DisplayName("게시글을_생성한다")
//	void createBoard() {
//		//given
//        FakeUuidIdentifierProvider fakeUuidIdentifierProvider = new FakeUuidIdentifierProvider(TEST_BOARD_GUID_1);
//
//		/**
//		 * 리뷰
//		 * 해당 boardService 재선언 불필요.
//		 */
////		boardService = new BoardService(
////				fakeUuidIdentifierProvider,
////				boardRepository
////		);
//
//    	//when
//		CreateBoardCommand createBoardCommand = new CreateBoardCommand(
//				BOARD_TITLE,
//				BOARD_CONTENT,
//				BOARD_CATEGORY_CD,
//				UserTestConstant.TEST_USER_GUID_1
//				);
//
//		boardService.createBoard(createBoardCommand);
//
//		//then
//		assertThat(boardRepository.findByBoardGuid(TEST_BOARD_GUID_1));
//
//
//	}
//}