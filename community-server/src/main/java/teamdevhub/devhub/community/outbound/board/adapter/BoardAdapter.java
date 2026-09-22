package teamdevhub.devhub.community.outbound.board.adapter;


import java.util.List;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import teamdevhub.devhub.community.core.board.domain.Board;
import teamdevhub.devhub.community.core.board.port.out.BoardRepository;
import teamdevhub.devhub.community.outbound.board.adapter.entity.BoardEntity;
import teamdevhub.devhub.community.outbound.board.adapter.mapper.BoardMapper;
import teamdevhub.devhub.community.outbound.board.persistence.JpaBoardRepository;
import teamdevhub.devhub.shared.outbound.common.exception.AdapterDataException;
import teamdevhub.devhub.shared.enums.ErrorCode;

@Component
@RequiredArgsConstructor
public class BoardAdapter implements BoardRepository {
	private final JpaBoardRepository jpaBoardRepository;
	
	@Override
	public void save(Board board) {
		jpaBoardRepository.save(BoardMapper.toEntity(board));
	}
	
	@Override
	public Board detailBoard(String boardGuid) {
		BoardEntity boardEntity = jpaBoardRepository.findByBoardGuid(boardGuid)
				.orElseThrow(() -> AdapterDataException.of(ErrorCode.READ_FAIL));
		
		return BoardMapper.toDomain(boardEntity);
	}
	
	@Override
	public void updateBoard(Board board) {
		jpaBoardRepository.save(BoardMapper.toEntity(board));
	}
	
	@Override
	public Board findByBoardGuid(String boardGuid) {
		BoardEntity boardEntity = jpaBoardRepository.findByBoardGuid(boardGuid)
				.orElseThrow(() -> AdapterDataException.of(ErrorCode.READ_FAIL));
		
		return BoardMapper.toDomain(boardEntity);
	}
	
	@Override
	public void updateViewCount(String boardGuid) {
		jpaBoardRepository.updateViewCount(boardGuid);
	}

	@Override
	public void deleteBoard(List<String> boardGuids) {
		jpaBoardRepository.deleteAllByBoardGuidIn(boardGuids);
	}
}
