package teamdevhub.devhub.web.api.board.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import teamdevhub.devhub.community.core.board.port.in.command.UpdateBoardCommand;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateBoardRequestDto {
	
	@NotBlank(message = "제목은 필수입니다")
	private String title;
	
	@NotBlank(message = "카테고리 설정은 필수입니다")
	private String categoryCd;
	
	@NotBlank(message = "내용은 필수입니다")
	@Size(max = 3000, message = "내용은 3000자를 초과할 수 없습니다")
	private String content;
	
	public UpdateBoardCommand toCommand(String userGuid, String boardGuid) {
		return UpdateBoardCommand.builder()
				.boardGuid(boardGuid)
				.title(this.title)
				.categoryCd(this.categoryCd)
				.content(this.content)
				.userGuid(userGuid)
				.build();
	}
	
}