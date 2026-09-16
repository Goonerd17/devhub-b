package teamdevhub.devhub.project.outbound.application.adapter.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import teamdevhub.devhub.platform.outbound.common.persistence.jpa.converter.BooleanToYNConverter;
import teamdevhub.devhub.platform.outbound.common.persistence.jpa.audit.BaseEntity;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "project_application")
public class ProjectApplicationEntity extends BaseEntity {
	
	@Id
	@Column(name = "application_guid", length = 32, nullable = false)	
	private String applicationGuid;

	@Column(name = "requirement_guid", length = 32, nullable = false)	
	private String requirementGuid;

	@Column(name = "applicant_guid", length = 32, nullable = false)	
	private String applicantGuid;

	@Column(name = "status_cd", nullable = false)	
	private String statusCd;

	@Column(name = "approver_guid", length = 32)	
	private String approverGuid;

	@Column(name = "decision_date")	
	private String decisionDate;

	@Convert(converter = BooleanToYNConverter.class)
	@Column(name = "cancel_yn", nullable = false)
	private boolean isCanceled;

	public void updateStatus(String statusCd, String approverGuid, String decisionDate) {
		this.statusCd = statusCd;
		this.approverGuid = approverGuid;
		this.decisionDate = decisionDate;
	}

	public void cancel() {
		this.isCanceled = true;
	}
}
