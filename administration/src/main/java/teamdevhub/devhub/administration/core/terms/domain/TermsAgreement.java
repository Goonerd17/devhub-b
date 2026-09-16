package teamdevhub.devhub.administration.core.terms.domain;

import lombok.Getter;

@Getter
public class TermsAgreement {

    private String termsAgreementGuid;
    private String termsGuid;
    private String userGuid;
    private boolean agreed;

    private TermsAgreement(
            String termsAgreementGuid,
            String termsGuid,
            String userGuid,
            boolean agreed
    ) {
        this.termsAgreementGuid = termsAgreementGuid;
        this.termsGuid = termsGuid;
        this.userGuid = userGuid;
        this.agreed = agreed;
    }

    public static TermsAgreement of(
            String termsAgreementGuid,
            String termsGuid,
            String userGuid,
            boolean agreed
    ) {
        return new TermsAgreement(
                termsAgreementGuid,
                termsGuid,
                userGuid,
                agreed
        );
    }
}
