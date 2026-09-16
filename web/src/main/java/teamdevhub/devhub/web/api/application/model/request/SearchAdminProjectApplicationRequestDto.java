package teamdevhub.devhub.web.api.application.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import teamdevhub.devhub.project.core.application.port.in.command.SearchAdminProjectApplicationCommand;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SearchAdminProjectApplicationRequestDto {
	
	private String approvalStatusCd;
	
	private String positionCd;
	
	private String levelCd;
	
	public SearchAdminProjectApplicationCommand toCommand(String projectGuid) {
		return SearchAdminProjectApplicationCommand.builder()
				.projectGuid(projectGuid)
				.approvalStatusCd(approvalStatusCd)
				.positionCd(positionCd)
				.levelCd(levelCd)
				.build();
	}
}
