package teamdevhub.devhub.administration.outbound.admin.form.adapter.mapper;

import teamdevhub.devhub.administration.core.admin.form.domain.ApplicationFormItem;
import teamdevhub.devhub.administration.outbound.admin.form.adapter.entity.ApplicationFormItemEntity;

public class ApplicationFormItemMapper {
	
	public static ApplicationFormItemEntity toEntity(ApplicationFormItem applicationFormItem) {
		return ApplicationFormItemEntity.builder()
				.formItemGuid(applicationFormItem.getFormItemGuid())
				.formGuid(applicationFormItem.getFormGuid())
				.content(applicationFormItem.getContent())
				.build();
	}
	
	public static ApplicationFormItem toApplicationFormItem(ApplicationFormItemEntity applicationFormItemEntity) {
		return ApplicationFormItem.builder()
				.formItemGuid(applicationFormItemEntity.getFormItemGuid())
				.formGuid(applicationFormItemEntity.getFormGuid())
				.content(applicationFormItemEntity.getContent())
				.build();
	}

}
