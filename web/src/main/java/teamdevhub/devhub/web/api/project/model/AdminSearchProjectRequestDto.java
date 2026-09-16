package teamdevhub.devhub.web.api.project.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import teamdevhub.devhub.project.core.project.port.in.command.AdminSearchProjectRequestCommand;
import teamdevhub.devhub.project.core.project.port.in.command.SearchProjectListCommand;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminSearchProjectRequestDto {

	private String keyword;
	private String recruitmentTypeCd;
	private String recruitStatusCd;
	private String progressTypeCd;
	private String progressRegionCd;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate recruitmentStartDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate recruitmentEndDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate progressStartDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate progressEndDate;
    
    public AdminSearchProjectRequestCommand toAdminSearchProjectRequestCommand() {
		return AdminSearchProjectRequestCommand.builder()
				.keyword(keyword)
				.recruitmentTypeCd(recruitmentTypeCd)
				.recruitStatusCd(recruitStatusCd)
				.progressTypeCd(progressTypeCd)
				.progressRegionCd(progressRegionCd)
				.recruitmentStartDate(recruitmentStartDate)
				.recruitmentEndDate(recruitmentEndDate)
				.progressStartDate(progressStartDate)
				.progressEndDate(progressEndDate)
				.build();
	}
}
