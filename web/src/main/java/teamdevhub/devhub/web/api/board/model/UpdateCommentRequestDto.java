package teamdevhub.devhub.web.api.board.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import teamdevhub.devhub.community.core.board.port.in.command.UpdateCommentCommand;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCommentRequestDto {
	
	@NotBlank(message = "내용은 필수입니다")
	private String content;
	
	public UpdateCommentCommand toCommand(String boardGuid, String commentGuid, String userGuid) {
		return UpdateCommentCommand.builder()
				.boardGuid(boardGuid)
				.content(this.content)
				.userGuid(userGuid)
				.commentGuid(commentGuid)
				.build();
	}
	
}