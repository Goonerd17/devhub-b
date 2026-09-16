package teamdevhub.devhub.administration.outbound.terms.adapter.mapper;

import teamdevhub.devhub.administration.core.terms.domain.Terms;
import teamdevhub.devhub.administration.core.terms.domain.TermsAgreement;
import teamdevhub.devhub.administration.outbound.terms.adapter.entity.TermsAgreementEntity;
import teamdevhub.devhub.administration.outbound.terms.adapter.entity.TermsEntity;

public class TermsMapper {

    private TermsMapper() {}

    public static Terms toDomain(TermsEntity termsEntity) {
        return Terms.of(
                termsEntity.getTermsGuid(),
                termsEntity.getTitle(),
                termsEntity.getContent(),
                termsEntity.isRequired(),
                termsEntity.isUsed(),
                termsEntity.isDeleted()
        );
    }

    public static TermsEntity toTermsEntity(Terms terms) {
        return TermsEntity.builder()
                .termsGuid(terms.getTermsGuid())
                .title(terms.getTitle())
                .content(terms.getContent())
                .isRequired(terms.isRequired())
                .isUsed(terms.isUsed())
                .isDeleted(terms.isDeleted())
                .build();
    }

    public static TermsAgreementEntity toEntity(TermsAgreement termsAgreement) {
        return TermsAgreementEntity.builder()
                .termsAgreementGuid(termsAgreement.getTermsAgreementGuid())
                .termsGuid(termsAgreement.getTermsGuid())
                .userGuid(termsAgreement.getUserGuid())
                .isAgreed(termsAgreement.isAgreed())
                .build();
    }
}
