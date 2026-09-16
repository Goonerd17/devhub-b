package teamdevhub.devhub.administration.core.terms.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.administration.core.terms.domain.TermsAgreement;

import static org.assertj.core.api.Assertions.assertThat;
import static teamdevhub.devhub.administration.constant.TermsTestConstant.*;

public class TermsAgreementTest {

    @Test
    @DisplayName("약관_동의_객체를_생성한다")
    void createTermsAgreement() {
        // when
        TermsAgreement termsAgreement = createAgreement(AGREEMENT_GUID_1, TERMS_GUID_1, USER_GUID_1, AGREED);

        // then
        assertThat(termsAgreement.getTermsAgreementGuid()).isEqualTo(AGREEMENT_GUID_1);
        assertThat(termsAgreement.getTermsGuid()).isEqualTo(TERMS_GUID_1);
        assertThat(termsAgreement.getUserGuid()).isEqualTo(USER_GUID_1);
        assertThat(termsAgreement.isAgreed()).isTrue();
    }

    @Test
    @DisplayName("약관_미동의_상태로도_생성할_수_있다")
    void createNotAgreedTermsAgreement() {
        // when
        TermsAgreement termsAgreement = createAgreement(AGREEMENT_GUID_2, TERMS_GUID_2, USER_GUID_2, NOT_AGREED);

        // then
        assertThat(termsAgreement.getTermsAgreementGuid()).isEqualTo(AGREEMENT_GUID_2);
        assertThat(termsAgreement.getTermsGuid()).isEqualTo(TERMS_GUID_2);
        assertThat(termsAgreement.getUserGuid()).isEqualTo(USER_GUID_2);
        assertThat(termsAgreement.isAgreed()).isFalse();
    }

    private TermsAgreement createAgreement(
            String termsAgreementGuid,
            String termsGuid,
            String userGuid,
            boolean agreed
    ) {
        return TermsAgreement.of(
                termsAgreementGuid,
                termsGuid,
                userGuid,
                agreed
        );
    }
}
