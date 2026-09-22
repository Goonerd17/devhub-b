package teamdevhub.devhub.project.http.application.model.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ProjectApplicationDetailWrapperResponseDto {

	private ProjectApplicationBasicResponseDto projectApplicationBasicDto;
	private List<ProjectApplicationAnswerDetailResponseDto> projectApplicationAnswerList;
}

