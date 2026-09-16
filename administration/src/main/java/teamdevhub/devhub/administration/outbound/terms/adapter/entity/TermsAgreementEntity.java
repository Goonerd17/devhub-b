package teamdevhub.devhub.administration.outbound.terms.adapter.entity;

import jakarta.persistence.*;
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
@Table(name = "terms_agreement")
public class TermsAgreementEntity extends BaseEntity {

    @Id
    @Column(length = 32)
    private String termsAgreementGuid;
    
    @Column(name = "terms_guid", nullable = false)
    private String termsGuid;
    
    @Column(name = "user_guid", nullable = false)
    private String userGuid;
    
    @Convert(converter = BooleanToYNConverter.class)
    @Column(name = "agreement_yn", nullable = false)
    private boolean isAgreed;
}