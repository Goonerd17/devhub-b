package teamdevhub.devhub.community.core.board.application;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import teamdevhub.devhub.community.core.board.domain.Board;
import teamdevhub.devhub.community.core.board.port.in.command.SearchAdminBoardCommand;
import teamdevhub.devhub.community.core.board.port.in.command.SearchBoardCommand;
import teamdevhub.devhub.community.core.board.port.in.usecase.BoardQueryUseCase;
import teamdevhub.devhub.community.core.board.port.out.BoardLikeRepository;
import teamdevhub.devhub.community.core.board.port.out.BoardQueryRepository;
import teamdevhub.devhub.community.core.board.port.out.CommentRepository;
import teamdevhub.devhub.platform.core.common.page.PageCommand;
import teamdevhub.devhub.platform.core.common.page.PageResult;
import teamdevhub.devhub.member.api.profile.MemberPublicProfile;
import teamdevhub.devhub.member.api.profile.MemberPublicProfileQuery;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardQueryService implements BoardQueryUseCase {
	
	private final BoardQueryRepository boardQueryRepository;
	private final BoardLikeRepository boardLikeRepository;
	private final CommentRepository commentRepository;
	private final MemberPublicProfileQuery memberPublicProfileQuery;
	
	@Override
	public PageResult<Board> listBoard(SearchBoardCommand searchBoardCommand, PageCommand pageCommand, String userGuid) {

		PageResult<Board> boardList = boardQueryRepository.listBoard(searchBoardCommand, pageCommand.page(), pageCommand.size());
		List<String> boardGuids = boardList.content().stream().map(Board::getBoardGuid).toList();
		List<String> userGuids = boardList.content().stream().map(Board::getUserGuid).toList();

		Map<String, Long> boardLikes = boardLikeRepository.countByLikeCount(boardGuids);
		Map<String, Long> boardComments = commentRepository.countByCommentCount(boardGuids);
		Map<String, String> userNames = memberPublicProfileQuery.findPublicProfilesByMemberGuids(userGuids).stream()
				.collect(java.util.stream.Collectors.toMap(MemberPublicProfile::memberGuid, MemberPublicProfile::displayName));
		Set<String> likedBoardGuids = new HashSet<>(boardLikeRepository.findLikedBoardGuids(userGuid, boardGuids));

		boardList.content().forEach(board ->
			board.fillSummarySubquery(boardLikes.getOrDefault(board.getBoardGuid(), 0L).toString(),
					boardComments.getOrDefault(board.getBoardGuid(), 0L).toString(),
					userNames.getOrDefault(board.getUserGuid(), ""),
					likedBoardGuids.contains(board.getBoardGuid())
					));

		return boardList;
	}
	
	@Override
	public PageResult<Board> listAdminBoard(SearchAdminBoardCommand searchAdminBoardCommand, PageCommand pageCommand) {
        return boardQueryRepository.listAdminBoard(searchAdminBoardCommand, pageCommand.page(), pageCommand.size());
	}
}
