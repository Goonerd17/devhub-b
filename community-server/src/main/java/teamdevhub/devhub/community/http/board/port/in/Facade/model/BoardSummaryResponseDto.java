package teamdevhub.devhub.community.http.board.port.in.Facade.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardSummaryResponseDto {
	
	private BoardBasicResponseDto boardBasicResponseDto;
	private String likeCount;
	private String commentCount;
	private boolean isLiked;
}

