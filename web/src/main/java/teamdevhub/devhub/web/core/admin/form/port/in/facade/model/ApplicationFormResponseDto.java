package teamdevhub.devhub.web.core.admin.form.port.in.facade.model;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import teamdevhub.devhub.administration.core.admin.form.port.in.command.ApplicationFormCommand;

@Getter
@Builder
public class ApplicationFormResponseDto {
	private String projectApplicationFormGuid;
	private String applicationFormGuid;
	private String typeCd;
	private String title;
	private String helpText;
	private String useYn;
	private boolean isCustomized;
	private boolean isUsed;
	private List<String> itemList;

	public static ApplicationFormResponseDto fromCommand(ApplicationFormCommand inputCommand) {
		return ApplicationFormResponseDto.builder()
				.applicationFormGuid(inputCommand.getApplicationFormGuid())
				.typeCd(inputCommand.getTypeCd())
				.title(inputCommand.getTitle())
				.helpText(inputCommand.getHelpText())
				.isCustomized(inputCommand.isCustomized())
				.isUsed(inputCommand.isUsed())
				.itemList(inputCommand.getItemList())
				.build();
	}
}