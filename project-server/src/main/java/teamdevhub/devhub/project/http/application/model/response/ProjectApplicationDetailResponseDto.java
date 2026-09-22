package teamdevhub.devhub.project.http.application.model.response;

import lombok.Builder;
import lombok.Getter;
import teamdevhub.devhub.project.core.application.domain.ProjectApplication;

import java.util.List;

@Getter
@Builder
public class ProjectApplicationDetailResponseDto {

	private String applicationGuid;
	private String requirementGuid;
	private String applicantGuid;
	private String approverGuid;
	private String decisionDate;
	private String statusCd;
	private String userName;
	private String email;
	private String mannerDegree;
	private List<String> skillList;
	private String position;
	private String levelCd;
	private String applyDate;

	public static ProjectApplicationDetailResponseDto fromDomain(ProjectApplication application) {
		return ProjectApplicationDetailResponseDto.builder()
			.applicationGuid(application.getApplicationGuid())
			.requirementGuid(application.getRequirementGuid())
			.applicantGuid(application.getApplicantGuid())
			.approverGuid(application.getApproverGuid())
			.decisionDate(application.getDecisionDate())
			.statusCd(application.getStatusCd())
			.userName(application.getUserName())
			.email(application.getEmail())
			.mannerDegree(String.valueOf(application.getMannerDegree()))
			.skillList(application.getUserSkillList())
			.position(application.getPositionCd())
			.levelCd(application.getLevelCd())
			.applyDate(application.getApplyDate())
			.build();
	}
}

