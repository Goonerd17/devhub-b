package teamdevhub.devhub.community.http.board.facade;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import teamdevhub.devhub.web.api.model.response.DataApiResponseDto;
import teamdevhub.devhub.community.http.response.DataListApiResponseDto;
import teamdevhub.devhub.community.http.response.PageResponseDto;
import teamdevhub.devhub.community.core.board.domain.Board;
import teamdevhub.devhub.community.http.board.port.in.Facade.model.AdminBoardResponseDto;
import teamdevhub.devhub.community.http.board.port.in.Facade.model.BoardBasicResponseDto;
import teamdevhub.devhub.community.http.board.port.in.Facade.model.BoardDetailResponseDto;
import teamdevhub.devhub.community.http.board.port.in.Facade.model.BoardSummaryResponseDto;
import teamdevhub.devhub.community.core.board.port.in.command.CreateBoardCommand;
import teamdevhub.devhub.community.core.board.port.in.command.SearchAdminBoardCommand;
import teamdevhub.devhub.community.core.board.port.in.command.SearchBoardCommand;
import teamdevhub.devhub.community.core.board.port.in.command.UpdateBoardCommand;
import teamdevhub.devhub.community.core.board.port.in.usecase.BoardLikeUseCase;
import teamdevhub.devhub.community.core.board.port.in.usecase.BoardQueryUseCase;
import teamdevhub.devhub.community.core.board.port.in.usecase.BoardUseCase;
import teamdevhub.devhub.shared.core.common.page.PageCommand;
import teamdevhub.devhub.shared.core.common.page.PageResult;
import teamdevhub.devhub.web.enums.SuccessCode;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardFacade {

    private final BoardQueryUseCase boardQueryUseCase;
    private final BoardUseCase boardUseCase;
    private final BoardLikeUseCase boardLikeUseCase;

	public DataListApiResponseDto<BoardSummaryResponseDto> listBoard(SearchBoardCommand searchBoardCommand, PageCommand pageCommand, String userGuid) {
		PageResult<Board> pagedBoardList = boardQueryUseCase.listBoard(searchBoardCommand, pageCommand, userGuid);

		List<BoardSummaryResponseDto> boardSummaryResponseDtoList = pagedBoardList.content().stream()
				.map(board -> BoardSummaryResponseDto.builder()
						.boardBasicResponseDto(BoardBasicResponseDto.fromDomain(board))
						.likeCount(board.getLikeCount())
						.commentCount(board.getCommentCount())
						.isLiked(board.isLiked())
		                .build())
				.toList();

		return DataListApiResponseDto.successWithDataList(
                        SuccessCode.READ_SUCCESS,
                        boardSummaryResponseDtoList,
                        PageResponseDto.from(pagedBoardList));
	}

	public DataApiResponseDto<Void> createBoard(CreateBoardCommand createBoardCommand) {
		boardUseCase.createBoard(createBoardCommand);

		return DataApiResponseDto.successWithoutData(
                        SuccessCode.CREATE_SUCCESS);
	}

	public DataApiResponseDto<BoardDetailResponseDto> detailBoard(String boardGuid, Boolean cookieResult, String userGuid) {
		Board board = boardUseCase.detailBoard(boardGuid, cookieResult, userGuid);

		BoardSummaryResponseDto summaryBoard = BoardSummaryResponseDto.builder()
				.boardBasicResponseDto(BoardBasicResponseDto.fromDomain(board))
				.likeCount(board.getLikeCount())
				.commentCount(board.getCommentCount())
                .build();

		BoardDetailResponseDto responseDto = BoardDetailResponseDto.builder()
				.boardSummaryResponseDto(summaryBoard)
				.commentList(board.getCommentList())
				.userEmail(board.getUserEmail())
				.isLiked(board.isLiked())
				.build();

		return DataApiResponseDto.successWithData(
				SuccessCode.READ_SUCCESS,
				responseDto
				);
	}

	public DataApiResponseDto<Void> updateBoard(UpdateBoardCommand updateBoardCommand) {
		boardUseCase.updateBoard(updateBoardCommand);

		return DataApiResponseDto.successWithoutData(
                        SuccessCode.UPDATE_SUCCESS);
	}

	public DataApiResponseDto<Void> likeBoard(String boardGuid, String userGuid) {
		boardLikeUseCase.likeBoard(boardGuid, userGuid);

		return DataApiResponseDto.successWithoutData(
                        SuccessCode.UPDATE_SUCCESS);
	}

	public DataApiResponseDto<Void> deleteBoard(String boardGuid, String userGuid) {
		boardUseCase.deleteBoard(boardGuid, userGuid);
		return DataApiResponseDto.successWithoutData(
                SuccessCode.DELETE_SUCCESS);
	}

	public DataApiResponseDto<Void> deleteAdminBoard(List<String> boardGuids) {
		boardUseCase.deleteAdminBoard(boardGuids);
		return DataApiResponseDto.successWithoutData(
                SuccessCode.DELETE_SUCCESS);
	}

	public DataListApiResponseDto<AdminBoardResponseDto> listAdminBoard(SearchAdminBoardCommand searchAdminBoardCommand, PageCommand pageCommand) {
		PageResult<Board> pagedBoardList = boardQueryUseCase.listAdminBoard(searchAdminBoardCommand, pageCommand);

		List<AdminBoardResponseDto> adminBoardResponseDtoList = pagedBoardList.content().stream()
				.map(board -> AdminBoardResponseDto.builder()
						.boardBasicResponseDto(BoardBasicResponseDto.fromDomain(board))
						.userstatus(board.getUserStatus())
						.reportCount(board.getReportCount())
		                .build())
				.toList();

		return DataListApiResponseDto.successWithDataList(
                        SuccessCode.READ_SUCCESS,
                        adminBoardResponseDtoList,
                        PageResponseDto.from(pagedBoardList));
	}

}
