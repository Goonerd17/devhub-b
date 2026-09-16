package teamdevhub.devhub.web.core.board.port.in.Facade.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import teamdevhub.devhub.community.core.board.domain.Comment;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardDetailResponseDto {
	
	private BoardSummaryResponseDto boardSummaryResponseDto;
	private List<Comment> commentList;
	private String userEmail;
	private boolean isLiked;
	
}
