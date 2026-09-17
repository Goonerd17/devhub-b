package teamdevhub.devhub.project.core.application.port.in.command;

import lombok.Builder;
import teamdevhub.devhub.project.core.project.domain.ProjectApprovalStatus;

@Builder
public record ApproveApplicationCommand(
	String applicationGuid,
	String approverGuid,
	boolean approved
) {
	public String resolveStatusCd() {
		return approved ? ProjectApprovalStatus.APPROVED.getCode() : ProjectApprovalStatus.REJECTED.getCode();
	}
}
