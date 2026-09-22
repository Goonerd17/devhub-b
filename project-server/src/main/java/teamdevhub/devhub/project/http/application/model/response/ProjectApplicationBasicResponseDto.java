package teamdevhub.devhub.project.http.application.model.response;

import lombok.Builder;
import lombok.Getter;
import teamdevhub.devhub.project.core.application.domain.ProjectApplication;

@Getter
@Builder
public class ProjectApplicationBasicResponseDto {

	private String applicationGuid;
	private String requirementGuid;
	private String applicantGuid;
	private String approverGuid;
	private String decisionDate;
	private String applyDate;

	public static ProjectApplicationBasicResponseDto fromDomain(ProjectApplication application) {
		return ProjectApplicationBasicResponseDto.builder()
			.applicationGuid(application.getApplicationGuid())
			.requirementGuid(application.getRequirementGuid())
			.applicantGuid(application.getApplicantGuid())
			.approverGuid(application.getApproverGuid())
			.decisionDate(application.getDecisionDate())
			.applyDate(application.getApplyDate())
			.build();
	}
}

