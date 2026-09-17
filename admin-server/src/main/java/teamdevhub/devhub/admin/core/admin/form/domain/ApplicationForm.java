package teamdevhub.devhub.admin.core.admin.form.domain;


import java.util.List;

import lombok.Builder;
import lombok.Getter;
import teamdevhub.devhub.admin.core.admin.form.port.in.command.CreateApplicationFormCommand;
import teamdevhub.devhub.admin.core.admin.form.port.in.command.SaveApplicationFormCommand;

@Getter
@Builder
public class ApplicationForm {

	private String applicationFormGuid;

	private String typeCd;

	private String title;

	private String helpText;

	private boolean isCustomized;

	private boolean isUsed;

	private List<String> items;
	
	public static ApplicationForm createCustomApplicationForm(CreateApplicationFormCommand createApplicationFormCommand, String applicationFormGuid) {
		return ApplicationForm.builder()
				.applicationFormGuid(applicationFormGuid)
				.typeCd(createApplicationFormCommand.getTypeCd())
				.title(createApplicationFormCommand.getTitle())
				.helpText(createApplicationFormCommand.getHelpText())
				.isCustomized(true)
				.isUsed(true)
				.build();
	}

	public static ApplicationForm createCustomApplicationForm(SaveApplicationFormCommand command, String applicationFormGuid) {
		return ApplicationForm.builder()
				.applicationFormGuid(applicationFormGuid)
				.typeCd(command.getTypeCd())
				.title(command.getTitle())
				.helpText(command.getHelpText())
				.isCustomized(!"Y".equals(command.getDefaultFieldYn()))
				.isUsed(command.isUsed())
				.build();
	}

	public void update(String title, String helpText, boolean isUsed, boolean isCustomized) {
		this.title = title;
		this.helpText = helpText;
		this.isUsed = isUsed;
		this.isCustomized = isCustomized;
	}
}
