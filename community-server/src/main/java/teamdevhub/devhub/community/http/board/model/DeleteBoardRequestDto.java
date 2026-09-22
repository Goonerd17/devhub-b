package teamdevhub.devhub.community.http.board.model;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeleteBoardRequestDto {

	@NotBlank(message = "게시글 GUID는 필수입니다")
	private String boardGuid;
}

