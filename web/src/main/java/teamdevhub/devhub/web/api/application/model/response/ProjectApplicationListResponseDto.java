package teamdevhub.devhub.web.api.application.model.response;

import lombok.Builder;
import lombok.Getter;
import teamdevhub.devhub.web.api.web.model.response.PageResponseDto;
import teamdevhub.devhub.web.core.project.port.in.facade.model.ProjectDetailResponseDto;

import java.util.List;

@Getter
@Builder
public class ProjectApplicationListResponseDto {

	private ProjectDetailResponseDto projectDetailDto;
	private List<ProjectApplicationDetailResponseDto> applicationList;
	private PageResponseDto pagination;
}
