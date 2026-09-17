package teamdevhub.devhub.auth.core.auth.application.selector;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.auth.core.auth.application.selector.verification.CompositeVerificationIssuerSelector;
import teamdevhub.devhub.shared.core.common.exception.BusinessRuleException;

import teamdevhub.devhub.auth.core.auth.port.out.verification.VerificationIssuer;
import teamdevhub.devhub.shared.shared.enums.ErrorCode;
import teamdevhub.devhub.auth.core.auth.application.service.verification.IssuedVerification;
import teamdevhub.devhub.auth.core.auth.domain.vo.verification.VerificationTarget;
import teamdevhub.devhub.auth.core.auth.domain.vo.verification.VerificationType;
import teamdevhub.devhub.fake.pure.application.issuer.FakeEmailVerificationIssuer;
import teamdevhub.devhub.fake.pure.application.provider.FakeTimeProvider;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class CompositeVerificationIssuerSelectorTest {

    private CompositeVerificationIssuerSelector compositeVerificationIssuerSelector;

    private static final LocalDateTime FIXED_TIME = LocalDateTime.of(2026,1,1,0,0);
    private static final String FIXED_CODE = "123456";

    @BeforeEach
    void init() {
        FakeTimeProvider fakeTimeProvider = new FakeTimeProvider(FIXED_TIME);

        VerificationIssuer emailVerificationIssuer = new FakeEmailVerificationIssuer(VerificationType.EMAIL, FIXED_CODE, fakeTimeProvider);

        compositeVerificationIssuerSelector = new CompositeVerificationIssuerSelector(List.of(emailVerificationIssuer));
    }

    @Test
    @DisplayName("EMAIL_타입이면_EMAIL_VerificationIssuer_가_선택되어_발급된다")
    void issueVerification_emailType_selectsEmailIssuer() {
        // given
        VerificationTarget verificationTarget = new VerificationTarget(VerificationType.EMAIL,"user@example.com");

        // when
        IssuedVerification issuedVerification = compositeVerificationIssuerSelector.issueVerification(verificationTarget);

        // then
        assertThat(issuedVerification.verification().getCode()).isEqualTo(FIXED_CODE);
        assertThat(issuedVerification.verification().getExpiredAt())
                .isEqualTo(FIXED_TIME.plusMinutes(5));
    }

    @Test
    @DisplayName("지원하지_않는_타입이면_BusinessRuleException_이_발생한다")
    void issueVerification_unsupportedType_throwsException() {
        // given
        VerificationTarget verificationTarget = new VerificationTarget(VerificationType.SMS,"01012341234");

        // when, then
        assertThatThrownBy(() ->
                compositeVerificationIssuerSelector.issueVerification(verificationTarget))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining(ErrorCode.VERIFICATION_FAIL.getMessage());
    }
}
