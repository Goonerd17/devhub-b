package teamdevhub.devhub.web.api.admin.board.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import teamdevhub.devhub.community.core.board.port.in.command.SearchAdminBoardCommand;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SearchAdminBoardRequestDto {
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate registeredStartDate;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate registeredEndDate;
	private String title;
	private String categoryCd;
    private String userStatus;
    private String reported;
	
	public SearchAdminBoardCommand toCommand() {
		LocalDateTime formattedStartDate = null;
		LocalDateTime formattedEndDate = null;
        if (registeredStartDate != null) {
            formattedStartDate = registeredStartDate.atStartOfDay();
        }
        if (registeredEndDate != null) {
            formattedEndDate = registeredEndDate.atTime(LocalTime.MAX);
        }
        
        return SearchAdminBoardCommand.builder()
                .registeredStartDate(formattedStartDate)
                .registeredEndDate(formattedEndDate)
                .title(title)
                .categoryCd(categoryCd)
                .userStatus(userStatus)
                .isReported("Y".equalsIgnoreCase(reported) ? Boolean.TRUE : "N".equalsIgnoreCase(reported) ? Boolean.FALSE : null)
                .build();
    }	
}
