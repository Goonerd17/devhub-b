package teamdevhub.devhub.web.api.project.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import teamdevhub.devhub.web.api.admin.form.model.CreateApplicationFormRequestDto;
import teamdevhub.devhub.administration.api.ApplicationFormDefinition;
import teamdevhub.devhub.project.core.project.domain.vo.command.UpdateProjectCommand;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateProjectRequestDto {
	
	@NotBlank(message = "사용자ID는 필수입니다")
	private String userGuid;
	
//	@NotBlank(message = "사용자이름은 필수입니다")
	private String username;
    
	@NotBlank(message = "제목은 필수입니다")
    private String title;
    
	@NotBlank(message = "카테고리는 필수입니다")
    private String category;
    
	@NotBlank(message = "상세내용은 필수입니다")
    @Size(max = 3000, message = "상세내용은 3000자를 초과할 수 없습니다")
    private String content;
	
	private String attachmentFileGuid;
	private String imageFileGuid;
    
	@NotBlank(message = "모집방식은 필수입니다")
    private String recruitmentTypeCd;
    
	@NotBlank(message = "진행방식은 필수입니다")
    private String progressTypeCd;
    
    private String progressRegionCd;
    
    @NotNull(message = "모집시작일은 필수입니다")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate recruitmentStartDate;
    
    @NotNull(message = "모집종료일은 필수입니다")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate recruitmentEndDate;
    
    @NotNull(message = "진행시작일은 필수입니다")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate progressStartDate;
    
    @NotNull(message = "진행종료일은 필수입니다")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate progressEndDate;
    
    @NotNull(message = "기술스택은 필수입니다")
    @Size(min = 1, message = "기술스택은 최소 1개 이상 선택해야 합니다")
    private List<String> skillList;
    
    @NotNull(message = "모집인원은 필수입니다")
    @Size(min = 1, message = "모집인원은 최소 1개 이상 선택해야 합니다")
    private List<CreateProjectRequirementRequestDto> positionList;
    
    @NotNull(message = "신청양식은 필수입니다")
    @Size(min = 1, message = "신청양식은 최소 1개 이상 선택해야 합니다")
    private List<String> applicationFormList;
    
    private List<CreateApplicationFormRequestDto> additionalFormList;
    
    public UpdateProjectCommand toCommand() {
        List<ApplicationFormDefinition> additionalFormCommands = null;

        if (this.additionalFormList != null) {
            additionalFormCommands = this.additionalFormList.stream()
                    .map(CreateApplicationFormRequestDto::toDefinition)
                    .toList();
        }

    	return UpdateProjectCommand.builder()
    			.userGuid(userGuid)
    			.username(username)
    			.title(this.title)
    			.category(this.category)
    			.content(this.content)
    			.imageFileGuid(this.imageFileGuid)
    			.attachmentFileGuid(this.attachmentFileGuid)
    			.recruitmentTypeCd(this.recruitmentTypeCd)
    			.progressTypeCd(this.progressTypeCd)
    			.progressRegionCd(this.progressRegionCd)
    			.recruitmentStartDate(this.recruitmentStartDate)
    			.recruitmentEndDate(this.recruitmentEndDate)
    			.progressStartDate(this.progressStartDate)
    			.progressEndDate(this.progressEndDate)
    			.skillList(this.skillList)
                .positionList(
                        this.positionList.stream()
                                .map(CreateProjectRequirementRequestDto::toCommand)
                                .collect(Collectors.toList())
                )
    			.applicationFormList(this.applicationFormList)
                .additionalFormList(additionalFormCommands)
    			.build();
    }

}
