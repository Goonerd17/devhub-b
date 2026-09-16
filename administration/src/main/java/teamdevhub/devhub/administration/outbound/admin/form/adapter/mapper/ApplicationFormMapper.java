package teamdevhub.devhub.administration.outbound.admin.form.adapter.mapper;

import teamdevhub.devhub.administration.core.admin.form.domain.ApplicationForm;
import teamdevhub.devhub.administration.outbound.admin.form.adapter.entity.ApplicationFormEntity;

public class ApplicationFormMapper {
	
	public static ApplicationFormEntity toEntity(ApplicationForm applicationForm) {
		return ApplicationFormEntity.builder()
				.applicationFormGuid(applicationForm.getApplicationFormGuid())
				.typeCd(applicationForm.getTypeCd())
				.title(applicationForm.getTitle())
				.helpText(applicationForm.getHelpText())
				.isCustomized(applicationForm.isCustomized())
				.isUsed(applicationForm.isUsed())
				.build();
	}
	
	public static ApplicationForm toApplicationForm(ApplicationFormEntity applicationFormEntity) {
		return ApplicationForm.builder()
				.applicationFormGuid(applicationFormEntity.getApplicationFormGuid())
				.typeCd(applicationFormEntity.getTypeCd())
				.title(applicationFormEntity.getTitle())
				.helpText(applicationFormEntity.getHelpText())
				.isCustomized(applicationFormEntity.isCustomized())
				.isUsed(applicationFormEntity.isUsed())
				.build();
	}

}
