package teamdevhub.devhub.community.outbound.board.adapter;

import java.util.List;

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
import teamdevhub.devhub.platform.core.common.page.PageResult;
import teamdevhub.devhub.community.outbound.board.adapter.entity.BoardEntity;
import teamdevhub.devhub.community.outbound.board.adapter.mapper.BoardMapper;
import teamdevhub.devhub.community.outbound.board.persistence.JpaBoardRepository;

@Component
@RequiredArgsConstructor
public class BoardQueryAdapter implements BoardQueryRepository {
	private final JpaBoardRepository jpaBoardRepository;
	
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
		Page<Object[]> pageBoardList = jpaBoardRepository.findBySearchCondition(				
				searchAdminBoardCommand.title(),
				searchAdminBoardCommand.categoryCd(),
				searchAdminBoardCommand.userStatus(),
				searchAdminBoardCommand.isReported(),
				searchAdminBoardCommand.registeredStartDate(),
				searchAdminBoardCommand.registeredEndDate(),
											pageable);
		
		List<Board> boards = pageBoardList.getContent().stream()
				.map(row -> {
					BoardEntity entity = (BoardEntity) row[0];
					String username = (String) row[1];
					boolean deleted = (boolean) row[2];
					boolean blocked = (boolean) row[3];
					Long reportCount = (Long) row[4];
		            
		            return (Board) BoardMapper.toAdminBoard(entity, username, deleted, blocked, reportCount);
				})
				.toList();
		return PageResult.of(
				boards, 
				pageBoardList.getNumber(),
				pageBoardList.getSize(),
				pageBoardList.getTotalElements()); 
	} 
}
