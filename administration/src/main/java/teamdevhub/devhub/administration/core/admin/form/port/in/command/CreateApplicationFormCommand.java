package teamdevhub.devhub.administration.core.admin.form.port.in.command;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateApplicationFormCommand {
	
	@NotBlank(message = "타입은 필수입니다.")
	private String typeCd;
	
	@NotBlank(message = "제목은 필수입니다.")
	private String title;
	
	private String helpText;
	
	private List<String> itemList;
}
