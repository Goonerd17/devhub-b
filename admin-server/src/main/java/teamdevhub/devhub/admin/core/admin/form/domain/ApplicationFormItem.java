package teamdevhub.devhub.admin.core.admin.form.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ApplicationFormItem {
	
	private String formItemGuid;
	private String formGuid;
	private String content;
	
	public static ApplicationFormItem createApplicationFormItem(String formItemGuid, String formGuid, String content) {
		return ApplicationFormItem.builder()
				.formItemGuid(formItemGuid)
				.formGuid(formGuid)
				.content(content)
				.build();
	}

}
