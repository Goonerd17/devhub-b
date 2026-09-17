package teamdevhub.devhub.admin.outbound.admin.form.adapter.entity;

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
@Table(name = "application_form_item")
public class ApplicationFormItemEntity {
	
	@Id
	@Column(name = "form_item_guid", length = 32, nullable = false)
	private String formItemGuid;
	
	@Column(name = "form_guid", length = 32, nullable = false)
	private String formGuid;
	
	@Column(name = "content", nullable = false)
	private String content;
}