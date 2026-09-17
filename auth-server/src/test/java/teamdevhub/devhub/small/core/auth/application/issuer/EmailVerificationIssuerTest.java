package teamdevhub.devhub.auth.core.auth.application.issuer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import teamdevhub.devhub.auth.core.auth.application.issuer.EmailVerificationIssuer;
import teamdevhub.devhub.auth.core.auth.application.service.verification.IssuedVerification;
import teamdevhub.devhub.auth.core.auth.domain.vo.verification.VerificationTarget;
import teamdevhub.devhub.auth.core.auth.domain.vo.verification.VerificationType;
import teamdevhub.devhub.fake.pure.application.provider.FakeTimeProvider;
import teamdevhub.devhub.fake.pure.application.provider.FakeVerificationCodeProvider;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class EmailVerificationIssuerTest {

    FakeVerificationCodeProvider fakeVerificationCodeProvider;
    FakeTimeProvider fakeTimeProvider;
    EmailVerificationIssuer emailVerificationIssuer;

    @BeforeEach
    void init() {
        fakeVerificationCodeProvider = new FakeVerificationCodeProvider("999999");
        fakeTimeProvider = new FakeTimeProvider(LocalDateTime.of(2026, 1, 18, 12, 0));
        emailVerificationIssuer = new EmailVerificationIssuer(fakeVerificationCodeProvider, fakeTimeProvider);
    }

    @Test
    @DisplayName("EMAIL_타입이면_true_를_반환한다")
    void supports_emailType_returnsTrue() {
        // given
        VerificationTarget verificationTarget = new VerificationTarget(VerificationType.EMAIL, "test@test.com");

        // when
        boolean result = emailVerificationIssuer.supports(verificationTarget);

        // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("SMS_타입이면_false_를_반환환다")
    void supports_smsType_returnsTrue() {
        // given
        VerificationTarget verificationTarget = new VerificationTarget(VerificationType.SMS, "01012341234");

        // when
        boolean result = emailVerificationIssuer.supports(verificationTarget);

        // then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("EMAIL_인증이면_코드와_만료시간이_포함된_IssuedVerification_을_반환한다")
    void issue_emailType_returnsIssuedVerification() {
        // given
        VerificationTarget verificationTarget = new VerificationTarget(VerificationType.EMAIL, "test@test.com");

        // when
        IssuedVerification issuedVerification = emailVerificationIssuer.issue(verificationTarget);

        // then
        assertThat(issuedVerification.verification().getCode()).isEqualTo("999999");
        assertThat(issuedVerification.verification().getExpiredAt())
                .isEqualTo(LocalDateTime.of(2026, 1, 18, 12, 0).plusMinutes(5));
    }
}
