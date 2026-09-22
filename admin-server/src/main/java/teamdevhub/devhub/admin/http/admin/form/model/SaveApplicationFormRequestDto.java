package teamdevhub.devhub.admin.http.admin.form.model;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import teamdevhub.devhub.admin.core.admin.form.port.in.command.SaveApplicationFormCommand;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SaveApplicationFormRequestDto {

	private String applicationFormGuid;

	@NotBlank(message = "필드명은 필수입니다.")
	private String fieldName;

	private String type;
	private List<@NotBlank(message = "선택항목 내용은 필수입니다.") String> options;
	private String helpYn;
	private String helpText;
	private String usedYn;
	private String defaultFieldYn;
	private Boolean insert;

	public SaveApplicationFormCommand toCommand() {
		return SaveApplicationFormCommand.builder()
				.applicationFormGuid(this.applicationFormGuid)
				.title(this.fieldName)
				.typeCd(this.type)
				.itemList(this.options)
				.helpYn(this.helpYn)
				.helpText(this.helpText)
				.isUsed("Y".equals(this.usedYn))
				.defaultFieldYn(this.defaultFieldYn)
				.insert(Boolean.TRUE.equals(this.insert))
				.build();
	}
}

