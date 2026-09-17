package teamdevhub.devhub.admin.core.terms.port.out;

import teamdevhub.devhub.admin.core.terms.domain.TermsAgreement;

import java.util.List;

public interface TermsAgreementRepository {

    void saveAll(List<TermsAgreement> termsAgreementList);
}
