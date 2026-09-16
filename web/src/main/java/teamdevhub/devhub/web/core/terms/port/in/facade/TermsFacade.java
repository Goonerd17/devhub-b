package teamdevhub.devhub.web.core.terms.port.in.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teamdevhub.devhub.administration.core.terms.domain.Terms;
import teamdevhub.devhub.administration.core.terms.port.in.command.CreateTermsCommand;
import teamdevhub.devhub.administration.core.terms.port.in.usecase.TermsUseCase;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class TermsFacade {

    private final TermsUseCase termsUseCase;

    public List<Terms> listTerms() {
        return termsUseCase.listTerms();
    }

    public void registerTerms(CreateTermsCommand createTermsCommand) {
        termsUseCase.registerTerms(createTermsCommand);
    }
}
