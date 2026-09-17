package teamdevhub.devhub.project.outbound.application.adapter.mapper;

import teamdevhub.devhub.project.core.application.domain.ProjectApplication;
import teamdevhub.devhub.project.core.application.domain.ProjectApplicationAnswer;
import teamdevhub.devhub.shared.core.common.audit.AuditInfo;
import teamdevhub.devhub.project.outbound.application.adapter.entity.ProjectApplicationAnswerEntity;
import teamdevhub.devhub.project.outbound.application.adapter.entity.ProjectApplicationEntity;
import teamdevhub.devhub.member.api.profile.MemberApplicationProfile;
import teamdevhub.devhub.project.outbound.project.adapter.entity.ProjectRequirementEntity;

import java.util.List;

public class ApplicationMapper {

	public static ProjectApplication toApplication(
		ProjectApplicationEntity applicationEntity,
		MemberApplicationProfile applicant,
		ProjectRequirementEntity requirementEntity,
		List<String> userSkillList
	) {
		return ProjectApplication.builder()
			.applicationGuid(applicationEntity.getApplicationGuid())
			.requirementGuid(applicationEntity.getRequirementGuid())
			.applicantGuid(applicationEntity.getApplicantGuid())
			.approverGuid(applicationEntity.getApproverGuid())
			.decisionDate(applicationEntity.getDecisionDate())
			.statusCd(applicationEntity.getStatusCd())
			.isCanceled(applicationEntity.isCanceled())
			.userName(applicant.displayName())
			//.email(applicantEntity.getEmail())
			.mannerDegree(applicant.mannerDegree())
			.userSkillList(userSkillList)
			.positionCd(requirementEntity.getPositionCd())
			.levelCd(requirementEntity.getLevelCd())
			.applyDate(applicationEntity.getRegisteredDate() != null
				? applicationEntity.getRegisteredDate().toLocalDate().toString()
				: null)
			.auditInfo(AuditInfo.of(
				applicationEntity.getRegistrantGuid(),
				applicationEntity.getRegisteredDate(),
				applicationEntity.getModifierGuid(),
				applicationEntity.getModifiedDate()
			))
			.build();
	}
	
	public static ProjectApplication toApplicationOnly(ProjectApplicationEntity applicationEntity) {
		return ProjectApplication.builder()
			.applicationGuid(applicationEntity.getApplicationGuid())
			.requirementGuid(applicationEntity.getRequirementGuid())
			.applicantGuid(applicationEntity.getApplicantGuid())
			.approverGuid(applicationEntity.getApproverGuid())
			.decisionDate(applicationEntity.getDecisionDate())
			.statusCd(applicationEntity.getStatusCd())
			.isCanceled(applicationEntity.isCanceled())
			.build();
	}

	public static ProjectApplicationEntity toApplicationEntity(ProjectApplication application) {
		return ProjectApplicationEntity.builder()
			.applicationGuid(application.getApplicationGuid())
			.requirementGuid(application.getRequirementGuid())
			.applicantGuid(application.getApplicantGuid())
			.approverGuid(application.getApproverGuid())
			.decisionDate(application.getDecisionDate())
			.statusCd(application.getStatusCd())
			.isCanceled(application.isCanceled())
			.build();
	}

	public static ProjectApplicationAnswerEntity toAnswerEntity(ProjectApplicationAnswer answer) {
		return ProjectApplicationAnswerEntity.builder()
			.projectApplicationFormGuid(answer.getProjectApplicationFormGuid())
			.applicationAnswerGuid(answer.getApplicationAnswerGuid())
			.applicationGuid(answer.getApplicationGuid())
			.applicationFormGuid(answer.getApplicationFormGuid())
			.projectGuid(answer.getProjectGuid())
			.content(answer.getContent())
			.fileGuid(answer.getFileGuid())
			.build();
	}
}
