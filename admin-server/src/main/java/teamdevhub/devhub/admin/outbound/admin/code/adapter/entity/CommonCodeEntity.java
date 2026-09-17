package teamdevhub.devhub.admin.outbound.admin.code.adapter.entity;

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
@Table(name = "common_code")
public class CommonCodeEntity extends BaseEntity {
	
	@Id
    @Column(name = "code_id", nullable = false)	
	private String codeId;
	
	@Column(name = "superior_code_id", nullable = false)	
	private String superiorCodeId;
	
    @Column(name = "name", nullable = false)	
	private String name;
	
    @Column(name = "sort_order")
	private String sortOrder;
	
	@Convert(converter = BooleanToYNConverter.class)
    @Column(name = "use_yn", nullable = false)
	private boolean isUsed;
	
    @Column(name = "remarks")	
	private String remarks;
}