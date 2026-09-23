package teamdevhub.devhub.auth.core.port.out;

import teamdevhub.devhub.shared.terms.AgreeTermsCommand;

public interface TermsAgreementPort {
    void save(AgreeTermsCommand command);
}
