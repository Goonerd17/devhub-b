package teamdevhub.devhub.admin.core.terms.port.in.usecase;

import teamdevhub.devhub.admin.core.terms.domain.Terms;
import teamdevhub.devhub.admin.api.terms.AgreeTermsCommand;
import teamdevhub.devhub.admin.core.terms.port.in.command.CreateTermsCommand;

import java.util.List;

public interface TermsUseCase {

    List<Terms> listTerms();
    void registerTerms(CreateTermsCommand createTermsCommand);
    void saveTermsAgreement(AgreeTermsCommand agreeTermsCommand);
}
