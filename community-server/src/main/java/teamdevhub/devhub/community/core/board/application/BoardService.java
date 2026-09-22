package teamdevhub.devhub.community.core.board.application;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import teamdevhub.devhub.community.core.board.domain.Board;
import teamdevhub.devhub.community.core.board.domain.Comment;
import teamdevhub.devhub.community.core.board.port.in.command.CreateBoardCommand;
import teamdevhub.devhub.community.core.board.port.in.command.UpdateBoardCommand;
import teamdevhub.devhub.community.core.board.port.in.usecase.BoardUseCase;
import teamdevhub.devhub.community.core.board.port.out.BoardLikeRepository;
import teamdevhub.devhub.community.core.board.port.out.BoardRepository;
import teamdevhub.devhub.community.core.board.port.out.CommentRepository;
import teamdevhub.devhub.shared.core.common.exception.BusinessRuleException;
import teamdevhub.devhub.shared.identifier.IdentifierProvider;
import teamdevhub.devhub.shared.security.MemberEmailQuery;
import teamdevhub.devhub.shared.member.MemberCommunityProfile;
import teamdevhub.devhub.shared.member.MemberProfileQuery;
import teamdevhub.devhub.shared.enums.ErrorCode;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardService implements BoardUseCase {
	
	private final IdentifierProvider identifierProvider;
	private final BoardRepository boardRepository;
	private final BoardLikeRepository boardLikeRepository;
	private final CommentRepository commentRepository;
	private final MemberProfileQuery memberCommunityProfileQuery;
	private final MemberEmailQuery memberEmailQuery;
	private final CommentService commentService;
	
	@Override
	public void createBoard(CreateBoardCommand createBoardCommand) {
		String boardGuid = identifierProvider.generateIdentifier();
		Board board = Board.createBoard(createBoardCommand, boardGuid);
		boardRepository.save(board);
	}
	
	@Override
	public Board detailBoard(String boardGuid, Boolean cookieResult, String userGuid) {
		if(cookieResult) { boardRepository.updateViewCount(boardGuid); }
		Board boardDetail = boardRepository.detailBoard(boardGuid);
		
		Map<String, Long> boardLikes = boardLikeRepository.countByLikeCount(List.of(boardDetail.getBoardGuid()));
		Map<String, Long> boardComments = commentRepository.countByCommentCount(List.of(boardDetail.getBoardGuid()));		
		MemberCommunityProfile user = memberCommunityProfileQuery.findCommunityProfiles(List.of(boardDetail.getUserGuid())).get(0);

        List<Comment> commentList = commentService.commentList(boardDetail.getBoardGuid());

        boolean isLiked = false;
        if (userGuid != null && !userGuid.isBlank()) {
            isLiked = boardLikeRepository.existsByBoardGuidAndUserGuid(boardGuid, userGuid);
        }
        
        String userEmail = memberEmailQuery.findEmail(user.memberGuid()).orElse(null);

        boardDetail.fillDetailSubquery(
				boardLikes.getOrDefault(boardDetail.getBoardGuid(), 0L).toString(),
				boardComments.getOrDefault(boardDetail.getBoardGuid(), 0L).toString(),
				user.displayName(),
				user.profileImageGuid(),
				userEmail,
				commentList,
				isLiked
        		);
        
        return boardDetail;
	}

	@Override
	public void updateBoard(UpdateBoardCommand updateBoardCommand) {
		Board board = boardRepository.findByBoardGuid(updateBoardCommand.boardGuid());
		validateOwner(board.getUserGuid(), updateBoardCommand.userGuid());
		board.update(
				updateBoardCommand.title(),
				updateBoardCommand.categoryCd(),
				updateBoardCommand.content()
				);
		boardRepository.updateBoard(board);
	}
	
	@Override
	public void deleteBoard(String boardGuid, String userGuid) {
		Board board = boardRepository.findByBoardGuid(boardGuid);
		validateOwner(board.getUserGuid(), userGuid);
		deleteBoardWithChildren(List.of(boardGuid));
	}

	@Override
	public void deleteAdminBoard(List<String> boardGuids) {
		deleteBoardWithChildren(boardGuids);
	}

	private void deleteBoardWithChildren(List<String> boardGuids) {
		commentRepository.deleteByBoardGuids(boardGuids);
		boardLikeRepository.deleteByBoardGuids(boardGuids);
		boardRepository.deleteBoard(boardGuids);
	}

	private void validateOwner(String ownerGuid, String requestUserGuid) {
		if (!ownerGuid.equals(requestUserGuid)) {
			throw BusinessRuleException.of(ErrorCode.AUTH_INVALID);
		}
	}
}
