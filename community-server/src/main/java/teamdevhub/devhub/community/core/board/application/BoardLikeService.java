package teamdevhub.devhub.community.core.board.application;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import teamdevhub.devhub.community.core.board.domain.BoardLike;
import teamdevhub.devhub.community.core.board.port.in.usecase.BoardLikeUseCase;
import teamdevhub.devhub.community.core.board.port.out.BoardLikeRepository;
import teamdevhub.devhub.shared.identifier.IdentifierProvider;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardLikeService implements BoardLikeUseCase {

	private final IdentifierProvider identifierProvider;
	private final BoardLikeRepository boardLikeRepository;

	@Override
	public void likeBoard(String boardGuid, String userGuid) {
		Optional<BoardLike> boardLike = boardLikeRepository.likeBoard(boardGuid, userGuid);

		if (boardLike.isPresent()) {
			boardLikeRepository.deleteBoardLike(boardLike.get());
		} else {
			String boardLikeGuid = identifierProvider.generateIdentifier();
			BoardLike newBoardLike = BoardLike.createBoardLike(boardGuid, userGuid, boardLikeGuid);
			boardLikeRepository.save(newBoardLike);
		}
	}

}
