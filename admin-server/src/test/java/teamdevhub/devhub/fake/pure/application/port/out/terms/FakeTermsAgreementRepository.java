package teamdevhub.devhub.fake.pure.application.port.out.terms;

import teamdevhub.devhub.admin.core.terms.domain.TermsAgreement;
import teamdevhub.devhub.admin.core.terms.port.out.TermsAgreementRepository;

import java.util.ArrayList;
import java.util.List;

public class FakeTermsAgreementRepository implements TermsAgreementRepository {

    private final List<TermsAgreement> store = new ArrayList<>();

    @Override
    public void saveAll(List<TermsAgreement> termsAgreementList) {
        store.addAll(termsAgreementList);
    }

    public List<TermsAgreement> findAll() {
        return store;
    }
}
