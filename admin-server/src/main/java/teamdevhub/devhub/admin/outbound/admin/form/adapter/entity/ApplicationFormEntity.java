package teamdevhub.devhub.admin.outbound.admin.form.adapter.entity;

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
import teamdevhub.devhub.shared.outbound.common.persistence.jpa.converter.BooleanToYNConverter;
import teamdevhub.devhub.shared.outbound.common.persistence.jpa.audit.BaseEntity;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "application_form")
public class ApplicationFormEntity extends BaseEntity {
	
	@Id
	@Column(name = "application_form_guid", length = 32, nullable = false)
	private String applicationFormGuid;

	@Column(name = "type_cd", nullable = false)
	private String typeCd;

	@Column(name = "title", nullable = false)
	private String title;

	@Column(name = "help_text")
	private String helpText;

	@Convert(converter = BooleanToYNConverter.class)
	@Column(name = "custom_yn", nullable = false)
	private boolean isCustomized;

	@Convert(converter = BooleanToYNConverter.class)
	@Column(name = "use_yn", nullable = false)
	private boolean isUsed;
}