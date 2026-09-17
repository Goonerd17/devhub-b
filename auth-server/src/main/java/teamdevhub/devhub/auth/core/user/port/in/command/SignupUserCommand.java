package teamdevhub.devhub.auth.core.user.port.in.command;

import lombok.Builder;
import teamdevhub.devhub.auth.core.auth.domain.vo.verification.VerificationTarget;
import teamdevhub.devhub.admin.api.terms.TermsAgreementItem;
import teamdevhub.devhub.admin.api.terms.AgreeTermsCommand;

import java.util.List;

@Builder
public record SignupUserCommand(String email,
                                String password,
                                String username,
                                String introduction,
                                List<String> positionList,
                                List<String> skillList,
                                List<TermsAgreementItem> termsAgreementItemList,
                                VerificationTarget verificationTarget) {


    public AgreeTermsCommand toAgreeTermsCommand(String userGuid) {
        return new AgreeTermsCommand(userGuid, this.termsAgreementItemList);
    }
}
