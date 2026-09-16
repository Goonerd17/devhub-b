package teamdevhub.devhub.administration.core.admin.form.port.in.command;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import teamdevhub.devhub.administration.core.admin.form.domain.ApplicationForm;
import teamdevhub.devhub.administration.core.admin.form.domain.ApplicationFormItem;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationFormCommand {
	private String applicationFormGuid; 
	private String typeCd; 
	private String title; 
	private String helpText; 
	private boolean isCustomized; 
	private boolean isUsed; 
	private List<String> itemList;
	
	public static ApplicationFormCommand fromDomain(ApplicationForm applicationForm, List<ApplicationFormItem> itemList) {
		return ApplicationFormCommand.builder()
				.applicationFormGuid(applicationForm.getApplicationFormGuid())
				.typeCd(applicationForm.getTypeCd())
				.title(applicationForm.getTitle())
				.helpText(applicationForm.getHelpText())
				.isCustomized(applicationForm.isCustomized())
				.isUsed(applicationForm.isUsed())
				.itemList(itemList.stream()
						.map(ApplicationFormItem::getContent)
						.toList())
				.build();
	}
}
