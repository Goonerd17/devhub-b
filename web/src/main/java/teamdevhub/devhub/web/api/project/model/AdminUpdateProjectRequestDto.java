package teamdevhub.devhub.web.api.project.model;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import teamdevhub.devhub.project.core.project.domain.vo.command.AdminUpdateProjectCommand;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminUpdateProjectRequestDto {

	@NotBlank(message = "제목은 필수입니다")
    private String title;
	
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
    
    public AdminUpdateProjectCommand toCommand() {
    	return AdminUpdateProjectCommand.builder()
    			.title(title)
    			.recruitmentTypeCd(recruitmentTypeCd)
    			.progressTypeCd(progressTypeCd)
    			.progressRegionCd(progressRegionCd)
    			.recruitmentStartDate(recruitmentStartDate)
    			.recruitmentEndDate(recruitmentEndDate)
    			.progressStartDate(progressStartDate)
    			.progressEndDate(progressEndDate)
    			.build();
    }
}

