package teamdevhub.devhub.project.core.application.domain;

import lombok.Builder;
import lombok.Getter;
import teamdevhub.devhub.platform.core.common.audit.AuditInfo;
import teamdevhub.devhub.platform.core.common.exception.DomainRuleException;
import teamdevhub.devhub.platform.shared.enums.ErrorCode;
import teamdevhub.devhub.project.core.project.domain.ProjectApprovalStatus;

import java.util.List;

@Getter
@Builder
public class ProjectApplication {

	private String applicationGuid;
	private String requirementGuid;
	private String applicantGuid;
	private String approverGuid;
	private String decisionDate;
	private String statusCd;
	private boolean isCanceled;

	// 지원자 유저 정보
	private String userName;
	private String email;
	private double mannerDegree;
	private List<String> userSkillList;

	// 모집 요건 정보
	private String positionCd;
	private String levelCd;

	private String applyDate;

	private AuditInfo auditInfo;

	// 지원 취소 시점의 검증 - 본인 지원 건만, 아직 승인/거절 처리되지 않은 건만 취소 가능
	public void assertCancelable(String requesterGuid) {
		if (!this.applicantGuid.equals(requesterGuid)) {
			throw DomainRuleException.of(ErrorCode.UPDATE_FAIL);
		}
		if (!ProjectApprovalStatus.PENDING.getCode().equals(this.statusCd)) {
			throw DomainRuleException.of(ErrorCode.APPLICATION_NOT_CANCELABLE);
		}
	}
}
