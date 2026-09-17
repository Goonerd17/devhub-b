package teamdevhub.devhub.project.outbound.application.adapter.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "project_application_form")
public class ProjectApplicationFormEntity {
	
	@Id
	@Column(name = "project_application_form_guid", length = 32, nullable = false)
	private String projectApplicationFormGuid;
	
	@Column(name = "project_guid", length = 32, nullable = false)
	private String projectGuid;
	
	@Column(name = "application_form_guid", length = 32, nullable = false)
	private String applicationFormGuid;
}