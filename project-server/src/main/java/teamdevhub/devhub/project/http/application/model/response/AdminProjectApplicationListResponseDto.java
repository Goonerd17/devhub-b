package teamdevhub.devhub.project.http.application.model.response;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import teamdevhub.devhub.project.http.response.PageResponseDto;

@Getter
@Builder
public class AdminProjectApplicationListResponseDto {

	private List<AdminProjectApplicationBasicResponseDto> applicantList;
	private PageResponseDto pagination;
}

