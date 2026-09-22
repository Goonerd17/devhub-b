package teamdevhub.devhub.auth.core.auth.port.in.command.oauth;

import lombok.Builder;
import teamdevhub.devhub.shared.terms.TermsAgreementItem;
import teamdevhub.devhub.shared.terms.AgreeTermsCommand;

import java.util.List;

@Builder
public record SignupOAuthUserCommand(String tempToken, String username, String introduction,
                                     List<String> positionList, List<String> skillList, List<TermsAgreementItem> termsAgreementItemList) {


    public AgreeTermsCommand toAgreeTermsCommand(String userGuid) {
        return new AgreeTermsCommand(userGuid, this.termsAgreementItemList);
    }

}
