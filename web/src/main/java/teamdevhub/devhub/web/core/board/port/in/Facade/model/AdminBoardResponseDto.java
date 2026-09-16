package teamdevhub.devhub.web.core.board.port.in.Facade.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminBoardResponseDto {
	
	private BoardBasicResponseDto boardBasicResponseDto;
	private String userstatus;
	private String reportCount;
}
