package teamdevhub.devhub.project.http.application.model;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import teamdevhub.devhub.shared.form.ApplicationFormView;

@Getter
@SuperBuilder
@NoArgsConstructor
public class ApplicationFormResponseDto {
	private String applicationFormGuid;
	private String title;
	private String typeCd;
	private String helpText;
	private String useYn;
	private String customYn;
	
	private String registrantGuid;
    private LocalDateTime registeredDate;
    private String modifierGuid;
    private LocalDateTime modifiedDate;
    
    public static ApplicationFormResponseDto fromView(ApplicationFormView applicationForm) {
    	return ApplicationFormResponseDto.builder()
				.applicationFormGuid(applicationForm.applicationFormGuid())
				.title(applicationForm.title())
				.typeCd(applicationForm.typeCd())
				.helpText(applicationForm.helpText())
				.useYn(applicationForm.used()?"Y":"N")
				.customYn(applicationForm.customized()?"Y":"N")
    			.build();
    }

}
