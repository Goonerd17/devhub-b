package teamdevhub.devhub.project.http.application.model.response;

import lombok.Builder;
import lombok.Getter;
import teamdevhub.devhub.project.core.application.domain.ProjectApplicationAnswer;

import java.util.List;

@Getter
@Builder
public class ProjectApplicationAnswerDetailResponseDto {

	private String applicationAnswerGuid;
	private String projectApplicationFormGuid;
	private String fileGuid;
	private String content;
	private String userName;
	private String email;
	private String mannerDegree;
	private List<String> userSkillList;
	private String positionCd;
	private String introduction;

	public static ProjectApplicationAnswerDetailResponseDto fromDomain(ProjectApplicationAnswer answer) {
		return ProjectApplicationAnswerDetailResponseDto.builder()
			.applicationAnswerGuid(answer.getApplicationAnswerGuid())
			.projectApplicationFormGuid(answer.getProjectApplicationFormGuid())
			.fileGuid(answer.getFileGuid())
			.content(answer.getContent())
			.userName(answer.getUserName())
			.email(answer.getEmail())
			.mannerDegree(String.valueOf(answer.getMannerDegree()))
			.userSkillList(answer.getUserSkillList())
			.positionCd(answer.getPositionCd())
			.introduction(answer.getIntroduction())
			.build();
	}
}

