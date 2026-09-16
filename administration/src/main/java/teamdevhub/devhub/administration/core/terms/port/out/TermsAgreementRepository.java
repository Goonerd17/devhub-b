package teamdevhub.devhub.administration.core.terms.port.out;

import teamdevhub.devhub.administration.core.terms.domain.TermsAgreement;

import java.util.List;

public interface TermsAgreementRepository {

    void saveAll(List<TermsAgreement> termsAgreementList);
}
