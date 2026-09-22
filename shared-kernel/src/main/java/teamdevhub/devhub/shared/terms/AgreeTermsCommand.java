package teamdevhub.devhub.shared.terms;

import java.util.List;

public record AgreeTermsCommand(String userGuid, List<TermsAgreementItem> termsAgreementItemList) {
}
