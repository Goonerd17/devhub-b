package teamdevhub.devhub.project.core.project.port.in.command;

import java.time.LocalDate;

import lombok.Builder;

@Builder
public record AdminSearchProjectRequestCommand(String keyword, String recruitmentTypeCd, String recruitStatusCd,
		String progressTypeCd, String progressRegionCd, LocalDate recruitmentStartDate,	LocalDate recruitmentEndDate,
		LocalDate progressStartDate, LocalDate progressEndDate) {

}
