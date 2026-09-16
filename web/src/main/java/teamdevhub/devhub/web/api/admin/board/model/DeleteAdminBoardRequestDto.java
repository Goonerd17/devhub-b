package teamdevhub.devhub.web.api.admin.board.model;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
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
public class DeleteAdminBoardRequestDto {

	@NotEmpty(message = "삭제할 게시글은 필수입니다")
	private List<@NotBlank(message = "게시글 GUID는 필수입니다") String> boardGuids;

}
