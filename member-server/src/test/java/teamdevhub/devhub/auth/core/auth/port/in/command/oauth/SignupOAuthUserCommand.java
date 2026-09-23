package teamdevhub.devhub.auth.core.auth.port.in.command.oauth;

import teamdevhub.devhub.shared.terms.TermsAgreementItem;
import java.util.List;

public record SignupOAuthUserCommand(String tempToken, String username, String introduction,
        List<String> positionList, List<String> skillList, List<TermsAgreementItem> termsAgreementItemList) {
    public static Builder builder() { return new Builder(); }
    public static final class Builder {
        private String tempToken, username, introduction;
        private List<String> positionList, skillList;
        private List<TermsAgreementItem> termsAgreementItemList;
        public Builder tempToken(String v) { tempToken = v; return this; }
        public Builder username(String v) { username = v; return this; }
        public Builder introduction(String v) { introduction = v; return this; }
        public Builder positionList(List<String> v) { positionList = v; return this; }
        public Builder skillList(List<String> v) { skillList = v; return this; }
        public Builder termsAgreementItemList(List<TermsAgreementItem> v) { termsAgreementItemList = v; return this; }
        public SignupOAuthUserCommand build() { return new SignupOAuthUserCommand(tempToken, username, introduction,
                positionList, skillList, termsAgreementItemList); }
    }
}
