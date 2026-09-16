package teamdevhub.devhub.administration.core.terms.application;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import teamdevhub.devhub.platform.core.common.exception.BusinessRuleException;
import teamdevhub.devhub.platform.identifier.IdentifierProvider;
import teamdevhub.devhub.administration.core.terms.domain.Terms;
import teamdevhub.devhub.administration.core.terms.domain.TermsAgreement;
import teamdevhub.devhub.administration.api.terms.TermsAgreementItem;
import teamdevhub.devhub.administration.api.terms.AgreeTermsCommand;
import teamdevhub.devhub.administration.core.terms.port.in.command.CreateTermsCommand;
import teamdevhub.devhub.administration.core.terms.port.in.usecase.TermsUseCase;
import teamdevhub.devhub.administration.core.terms.port.out.TermsAgreementRepository;
import teamdevhub.devhub.administration.core.terms.port.out.TermsRepository;
import teamdevhub.devhub.platform.shared.enums.ErrorCode;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class TermsService implements TermsUseCase {

    private final TermsRepository termsRepository;
    private final TermsAgreementRepository termsAgreementRepository;
    private final IdentifierProvider identifierProvider;

    @Override
    public List<Terms> listTerms() {
        return termsRepository.listTerms();
    }

    @Override
    public void registerTerms(CreateTermsCommand createTermsCommand) {
        Terms terms = Terms.createTerms(createTermsCommand, identifierProvider.generateIdentifier());
        termsRepository.saveTerms(terms);
    }

    @Override
    public void saveTermsAgreement(AgreeTermsCommand agreeTermsCommand) {

        Map<String, Boolean> termsAgreementMap = toAgreementMap(agreeTermsCommand);

        List<Terms> termsList = termsRepository.findAllByTermsGuidIn(termsAgreementMap.keySet());
        validateAllTermsExist(termsList, termsAgreementMap);

        List<TermsAgreement> termsAgreementList = termsList.stream()
                .map(terms -> Terms.createAgreement(
                        terms,
                        identifierProvider.generateIdentifier(),
                        agreeTermsCommand.userGuid(),
                        termsAgreementMap.get(terms.getTermsGuid())
                ))
                .toList();

        termsAgreementRepository.saveAll(termsAgreementList);
    }

    private Map<String, Boolean> toAgreementMap(AgreeTermsCommand agreeTermsCommand) {
        return agreeTermsCommand.termsAgreementItemList()
                .stream()
                .collect(Collectors.toMap(
                        TermsAgreementItem::termsGuid,
                        TermsAgreementItem::agreed
                ));
    }

    private void validateAllTermsExist(List<Terms> termsList, Map<String, Boolean> termsAgreementMap) {
        Set<String> foundTermsGuids = termsList.stream()
                .map(Terms::getTermsGuid)
                .collect(Collectors.toSet());

        if (!foundTermsGuids.equals(termsAgreementMap.keySet())) {
            throw BusinessRuleException.of(ErrorCode.UNKNOWN_FAIL);
        }
    }
}
