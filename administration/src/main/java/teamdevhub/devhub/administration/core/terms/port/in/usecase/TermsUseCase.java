package teamdevhub.devhub.administration.core.terms.port.in.usecase;

import teamdevhub.devhub.administration.core.terms.domain.Terms;
import teamdevhub.devhub.administration.api.terms.AgreeTermsCommand;
import teamdevhub.devhub.administration.core.terms.port.in.command.CreateTermsCommand;

import java.util.List;

public interface TermsUseCase {

    List<Terms> listTerms();
    void registerTerms(CreateTermsCommand createTermsCommand);
    void saveTermsAgreement(AgreeTermsCommand agreeTermsCommand);
}
