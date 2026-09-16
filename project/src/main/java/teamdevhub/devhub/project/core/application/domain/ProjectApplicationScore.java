package teamdevhub.devhub.project.core.application.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProjectApplicationScore {
	
	private String applicantGuid;
	private String userName;
	private String email;
	private String statusCd;
	private double mannerDegree;
	private Double score;
	
	public static ProjectApplicationScore toApplicationWithScore(ProjectApplication application, Double score) {
		return ProjectApplicationScore.builder()
				.applicantGuid(application.getApplicantGuid())
				.userName(application.getUserName())
				.email(application.getEmail())
				.statusCd(application.getStatusCd())
				.mannerDegree(application.getMannerDegree())
				.score(score)
				.build();
	}
}
