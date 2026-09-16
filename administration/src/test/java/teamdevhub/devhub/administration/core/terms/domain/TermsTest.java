package teamdevhub.devhub.administration.core.terms.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.platform.core.common.exception.DomainRuleException;
import teamdevhub.devhub.administration.core.terms.domain.Terms;
import teamdevhub.devhub.administration.core.terms.domain.TermsAgreement;
import teamdevhub.devhub.administration.core.terms.port.in.command.CreateTermsCommand;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static teamdevhub.devhub.administration.constant.TermsTestConstant.*;

class TermsTest {

    @Test
    @DisplayName("약관을_생성한다")
    void createTerms() {
        // given
        CreateTermsCommand createTermsCommand = createCommand(REQUIRED, USED, NOT_DELETED);

        // when
        Terms terms = Terms.createTerms(createTermsCommand, TERMS_GUID_1);

        // then
        assertThat(terms.getTermsGuid()).isEqualTo(TERMS_GUID_1);
        assertThat(terms.getTitle()).isEqualTo(TERMS_TITLE_1);
        assertThat(terms.getContent()).isEqualTo(TERMS_CONTENT_1);
        assertThat(terms.isRequired()).isTrue();
        assertThat(terms.isUsed()).isTrue();
        assertThat(terms.isDeleted()).isFalse();
    }

    @Test
    @DisplayName("삭제된_약관이면_동의시_예외를_던진다")
    void throwIfDeletedTerms() {
        // given
        Terms terms = createTerms(REQUIRED, USED, DELETED);

        // when & then
        assertThatThrownBy(() -> terms.validateAgreement(true))
                .isInstanceOf(DomainRuleException.class)
                .hasMessageContaining("유효하지 않은 약관입니다");
    }

    @Test
    @DisplayName("사용중이_아닌_약관이면_동의시_예외를_던진다")
    void throwIfNotUsedTerms() {
        // given
        Terms terms = createTerms(REQUIRED, NOT_USED, NOT_DELETED);

        // when & then
        assertThatThrownBy(() -> terms.validateAgreement(true))
                .isInstanceOf(DomainRuleException.class)
                .hasMessageContaining("유효하지 않은 약관입니다");
    }

    @Test
    @DisplayName("필수_약관인데_동의하지_않으면_예외를_던진다")
    void throwIfRequiredButNotAgreed() {
        // given
        Terms terms = createTerms(REQUIRED, USED, NOT_DELETED);

        // when & then
        assertThatThrownBy(() -> terms.validateAgreement(false))
                .isInstanceOf(DomainRuleException.class)
                .hasMessageContaining("유효하지 않은 약관 동의입니다");
    }

    @Test
    @DisplayName("선택_약관은_동의하지_않아도_예외가_발생하지_않는다")
    void optionalTermsCanBeNotAgreed() {
        // given
        Terms terms = createTerms(OPTIONAL, USED, NOT_DELETED);

        // when
        terms.validateAgreement(false);
    }

    @Test
    @DisplayName("약관에_동의하면_TermsAgreement_를_생성한다")
    void createAgreement() {
        // given
        Terms terms = createTerms(REQUIRED, USED, NOT_DELETED);

        // when
        TermsAgreement termsAgreement = Terms.createAgreement(
                terms,
                AGREEMENT_GUID_1,
                USER_GUID_1,
                true
        );

        // then
        assertThat(termsAgreement.getTermsAgreementGuid()).isEqualTo(AGREEMENT_GUID_1);
        assertThat(termsAgreement.getTermsGuid()).isEqualTo(TERMS_GUID_1);
        assertThat(termsAgreement.getUserGuid()).isEqualTo(USER_GUID_1);
        assertThat(termsAgreement.isAgreed()).isTrue();
    }

    private Terms createTerms(boolean isRequired, boolean isUsed, boolean isDeleted) {
        return Terms.of(
                TERMS_GUID_1,
                TERMS_TITLE_1,
                TERMS_CONTENT_1,
                isRequired,
                isUsed,
                isDeleted
        );
    }

    private CreateTermsCommand createCommand(boolean isRequired, boolean isUsed, boolean isDeleted) {
        return CreateTermsCommand.builder()
                .title(TERMS_TITLE_1)
                .content(TERMS_CONTENT_1)
                .isRequired(isRequired)
                .isUsed(isUsed)
                .isDeleted(isDeleted)
                .build();
    }
}
