package teamdevhub.devhub.admin.core.terms.port.out;

import teamdevhub.devhub.admin.core.terms.domain.Terms;

import java.util.List;
import java.util.Set;

public interface TermsRepository {

    List<Terms> listTerms();
    void saveTerms(Terms terms);
    List<Terms> findAllByTermsGuidIn(Set<String> termsGuids);
    Terms findByTermsGuid(String termsGuid);
}