package teamdevhub.devhub.web.core.admin.form.port.in.facade.model;

import lombok.Builder;
import lombok.Getter;
import teamdevhub.devhub.administration.core.admin.form.domain.ApplicationFormItem;

@Getter
@Builder
public class ApplicationFormItemResponseDto {
	private String formItemGuid;
	private String formGuid;
	private String content;
	
	public static ApplicationFormItemResponseDto fromDomain(ApplicationFormItem inputDomain) {
		return ApplicationFormItemResponseDto.builder()
				.formItemGuid(inputDomain.getFormItemGuid())
				.formGuid(inputDomain.getFormGuid())
				.content(inputDomain.getContent())
				.build();
	}
}
