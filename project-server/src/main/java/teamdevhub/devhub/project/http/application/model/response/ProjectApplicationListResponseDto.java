package teamdevhub.devhub.project.http.application.model.response;

import lombok.Builder;
import lombok.Getter;
import teamdevhub.devhub.project.http.response.PageResponseDto;
import teamdevhub.devhub.project.http.project.model.ProjectDetailResponseDto;

import java.util.List;

@Getter
@Builder
public class ProjectApplicationListResponseDto {

	private ProjectDetailResponseDto projectDetailDto;
	private List<ProjectApplicationDetailResponseDto> applicationList;
	private PageResponseDto pagination;
}

