package teamdevhub.devhub.admin.outbound.terms.adapter.mapper;

import teamdevhub.devhub.admin.core.terms.domain.TermsAgreement;
import teamdevhub.devhub.admin.outbound.terms.adapter.entity.TermsAgreementEntity;

public class TermsAgreementMapper {

    public static TermsAgreementEntity toTermsAgreementEntity(TermsAgreement termsAgreement) {
        return TermsAgreementEntity.builder()
                .termsAgreementGuid(termsAgreement.getTermsAgreementGuid())
                .termsGuid(termsAgreement.getTermsGuid())
                .userGuid(termsAgreement.getUserGuid())
                .isAgreed(termsAgreement.isAgreed())
                .build();
    }

    public static TermsAgreement toTermsAgreement(TermsAgreementEntity termsAgreementEntity) {
        return TermsAgreement.of(
                termsAgreementEntity.getTermsAgreementGuid(),
                termsAgreementEntity.getTermsGuid(),
                termsAgreementEntity.getUserGuid(),
                termsAgreementEntity.isAgreed()
        );
    }
}
