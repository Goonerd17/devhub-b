package teamdevhub.devhub.administration.api.terms;

import lombok.Builder;

import java.util.List;

@Builder
public record AgreeTermsCommand(String userGuid, List<TermsAgreementItem> termsAgreementItemList) {
}
