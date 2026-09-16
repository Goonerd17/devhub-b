package teamdevhub.devhub.web.api.board.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import teamdevhub.devhub.community.core.board.port.in.command.SearchBoardCommand;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SearchBoardRequestDto {
	
	private String title;
	private String categoryCd;
	
	public SearchBoardCommand toCommand() {
		return this.toCommand(null);
	}
	
	public SearchBoardCommand toCommand(String userGuid) {
		return SearchBoardCommand.builder()
				.title(this.title)
				.categoryCd(this.categoryCd)
				.userGuid(userGuid)
				.build();
	}
	
}
