package teamdevhub.devhub.admin.core.terms.domain;

import lombok.Builder;
import lombok.Getter;
import teamdevhub.devhub.shared.core.common.exception.DomainRuleException;
import teamdevhub.devhub.admin.core.terms.port.in.command.CreateTermsCommand;
import teamdevhub.devhub.shared.enums.ErrorCode;

@Getter
public class Terms {

    private final String termsGuid;
    private final String title;
    private final String content;
    private final boolean isRequired;
    private final boolean isUsed;
    private final boolean isDeleted;

    @Builder
    private Terms(
            String termsGuid,
            String title,
            String content,
            boolean isRequired,
            boolean isUsed,
            boolean isDeleted
    ) {
        this.termsGuid = termsGuid;
        this.title = title;
        this.content = content;
        this.isRequired = isRequired;
        this.isUsed = isUsed;
        this.isDeleted = isDeleted;
    }

    public static Terms of(
            String termsGuid,
            String title,
            String content,
            boolean isRequired,
            boolean isUsed,
            boolean isDeleted
    ) {
        return Terms.builder()
                .termsGuid(termsGuid)
                .title(title)
                .content(content)
                .isRequired(isRequired)
                .isUsed(isUsed)
                .isDeleted(isDeleted)
                .build();
    }

    public void validateAgreement(boolean agreed) {

        if (isDeleted) {
            throw DomainRuleException.of(ErrorCode.INVALID_TERMS);
        }

        if (!isUsed) {
            throw DomainRuleException.of(ErrorCode.INVALID_TERMS);
        }

        if (isRequired && !agreed) {
            throw DomainRuleException.of(ErrorCode.INVALID_TERMS_AGREEMENT);
        }
    }

    public static Terms createTerms(CreateTermsCommand createTermsCommand, String termsGuid) {
        return Terms.builder()
                .termsGuid(termsGuid)
                .title(createTermsCommand.title())
                .content(createTermsCommand.content())
                .isRequired(createTermsCommand.isRequired())
                .isUsed(createTermsCommand.isUsed())
                .isDeleted(createTermsCommand.isDeleted())
                .build();
    }

    public static TermsAgreement createAgreement(
            Terms terms,
            String termsAgreementGuid,
            String userGuid,
            boolean agreed
    ) {
        terms.validateAgreement(agreed);

        return TermsAgreement.of(
                termsAgreementGuid,
                terms.getTermsGuid(),
                userGuid,
                agreed
        );
    }
}
