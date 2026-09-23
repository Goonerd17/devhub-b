package teamdevhub.devhub.community.outbound.board.adapter;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import teamdevhub.devhub.community.core.board.domain.Board;
import teamdevhub.devhub.community.core.board.port.in.command.SearchAdminBoardCommand;
import teamdevhub.devhub.community.core.board.port.in.command.SearchBoardCommand;
import teamdevhub.devhub.community.core.board.port.out.BoardQueryRepository;
import teamdevhub.devhub.shared.core.common.page.PageResult;
import teamdevhub.devhub.community.outbound.board.adapter.entity.BoardEntity;
import teamdevhub.devhub.community.outbound.board.adapter.mapper.BoardMapper;
import teamdevhub.devhub.community.outbound.board.persistence.JpaBoardRepository;
import teamdevhub.devhub.shared.member.MemberModerationProfile;
import teamdevhub.devhub.community.core.port.out.MemberModerationPort;

@Component
@RequiredArgsConstructor
public class BoardQueryAdapter implements BoardQueryRepository {
	private final JpaBoardRepository jpaBoardRepository;
	private final MemberModerationPort memberModerationQuery;
	
	@Override
	public PageResult<Board> listBoard(SearchBoardCommand searchBoardCommand, int page, int size) {
		Pageable pageable = PageRequest.of(page, size, Sort.by("registeredDate").descending().and(Sort.by("boardGuid")));
		Page<BoardEntity> pageBoardList = jpaBoardRepository.findByConditions(				
											searchBoardCommand.title(),
											searchBoardCommand.categoryCd(),
											searchBoardCommand.userGuid(),
											pageable);

		PageResult<Board> aa = PageResult.of(
				pageBoardList.getContent().stream().map(BoardMapper::toBoard).toList(), 
				pageBoardList.getNumber(),
				pageBoardList.getSize(),
				pageBoardList.getTotalElements());
		 return aa;
	} 
	
	@Override
	public PageResult<Board> listAdminBoard(SearchAdminBoardCommand searchAdminBoardCommand, int page, int size) {
		Pageable pageable = PageRequest.of(page, size, Sort.by("registeredDate").descending());
		boolean filterByUserStatus = searchAdminBoardCommand.userStatus() != null;
		List<String> memberGuids = filterByUserStatus
				? memberModerationQuery.findMemberGuidsByStatus(searchAdminBoardCommand.userStatus())
				: List.of("__status_filter_not_applied__");
		if (filterByUserStatus && memberGuids.isEmpty()) {
			return PageResult.of(List.of(), page, size, 0);
		}
		Page<Object[]> pageBoardList = jpaBoardRepository.findBySearchCondition(				
				searchAdminBoardCommand.title(),
				searchAdminBoardCommand.categoryCd(),
				filterByUserStatus,
				memberGuids,
				searchAdminBoardCommand.isReported(),
				searchAdminBoardCommand.registeredStartDate(),
				searchAdminBoardCommand.registeredEndDate(),
											pageable);
		
		List<String> pageMemberGuids = pageBoardList.getContent().stream()
				.map(row -> ((BoardEntity) row[0]).getUserGuid()).distinct().toList();
		Map<String, MemberModerationProfile> profiles = memberModerationQuery
				.findModerationProfiles(pageMemberGuids).stream()
				.collect(Collectors.toMap(MemberModerationProfile::memberGuid, Function.identity()));

		List<Board> boards = pageBoardList.getContent().stream()
				.map(row -> {
					BoardEntity entity = (BoardEntity) row[0];
					MemberModerationProfile profile = profiles.get(entity.getUserGuid());
					Long reportCount = (Long) row[1];
		            
		            return BoardMapper.toAdminBoard(entity,
							profile == null ? "" : profile.displayName(),
							profile != null && profile.deleted(),
							profile != null && profile.blocked(),
							reportCount);
				})
				.toList();
		return PageResult.of(
				boards, 
				pageBoardList.getNumber(),
				pageBoardList.getSize(),
				pageBoardList.getTotalElements()); 
	} 
}
