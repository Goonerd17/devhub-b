package teamdevhub.devhub.admin.outbound.terms.adapter.entity;

import jakarta.persistence.*;
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
@Table(name = "terms")
public class TermsEntity extends BaseEntity {

    @Id
    @Column(length = 32)
    private String termsGuid;
    
    @Column(name = "name", nullable = false, columnDefinition = "LONGTEXT")
    private String title;

    @Lob
    @Column(name = "content", nullable = false, columnDefinition = "LONGTEXT")
    private String content;
    
    @Convert(converter = BooleanToYNConverter.class)
    @Column(name = "required_yn", nullable = false)
    private boolean isRequired;
    
    @Convert(converter = BooleanToYNConverter.class)
    @Column(name = "use_yn", nullable = false)
    private boolean isUsed;
    
    @Convert(converter = BooleanToYNConverter.class)
    @Column(name = "delete_yn", nullable = false)
    private boolean isDeleted;
}