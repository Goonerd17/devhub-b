package teamdevhub.devhub.web.api.board.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import teamdevhub.devhub.community.core.board.port.in.command.CreateCommentCommand;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateCommentRequestDto {
	
	@NotBlank(message = "내용은 필수입니다")
	private String content;
	
	public CreateCommentCommand toCommand(String userGuid, String boardGuid) {
		return CreateCommentCommand.builder()
				.boardGuid(boardGuid)
				.content(this.content)
				.userGuid(userGuid)
				.build();
	}
	
}