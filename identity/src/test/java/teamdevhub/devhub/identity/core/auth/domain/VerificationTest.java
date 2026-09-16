package teamdevhub.devhub.identity.core.auth.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.platform.core.common.exception.DomainRuleException;
import teamdevhub.devhub.identity.core.auth.domain.Verification;
import teamdevhub.devhub.identity.core.auth.domain.vo.verification.VerificationMessage;
import teamdevhub.devhub.identity.core.auth.domain.vo.verification.VerificationTarget;
import teamdevhub.devhub.identity.core.auth.domain.vo.verification.VerificationType;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

public class VerificationTest {

    private static final String TEST_EMAIL_1 = "user1@example.com";
    private static final String TEST_EMAIL_CODE = "123456";
    private static final VerificationTarget VERIFICATION_TARGET_1 = new VerificationTarget(VerificationType.EMAIL, TEST_EMAIL_1);

    @Test
    @DisplayName("?몄쬆??諛쒓툒?섎㈃_誘몄씤利??곹깭?대떎")
    void issueVerificationIsUnverified() {
        // given
        LocalDateTime expiredAt = LocalDateTime.now().plusMinutes(5);
        VerificationTarget verificationTarget = new VerificationTarget(VerificationType.EMAIL, TEST_EMAIL_1);
        VerificationMessage verificationMessage = new VerificationMessage(TEST_EMAIL_CODE, expiredAt);

        // when
        Verification verification = Verification.issue(verificationTarget, verificationMessage);

        // then
        assertThat(verification.isVerified()).isFalse();
        assertThat(verification.getVerificationTarget()).isEqualTo(VERIFICATION_TARGET_1);
        assertThat(verification.getCode()).isEqualTo(TEST_EMAIL_CODE);
        assertThat(verification.getExpiredAt()).isEqualTo(expiredAt);
    }

    @Test
    @DisplayName("?щ컮瑜?肄붾뱶?닿퀬_留뚮즺?섏?_?딆븯?ㅻ㈃_?몄쬆???깃났?쒕떎")
    void confirm_success_whenCodeIsCorrectAndNotExpired() {
        // given
        LocalDateTime now = LocalDateTime.now();
        VerificationTarget verificationTarget = new VerificationTarget(VerificationType.EMAIL, TEST_EMAIL_1);
        VerificationMessage verificationMessage = new VerificationMessage(TEST_EMAIL_CODE, LocalDateTime.now().plusHours(1));
        Verification verification = Verification.issue(verificationTarget, verificationMessage);

        // when
        verification.confirm(TEST_EMAIL_CODE, now);

        // then
        assertThat(verification.isVerified()).isTrue();
    }

    @Test
    @DisplayName("?섎せ???몄쬆肄붾뱶瑜??낅젰?섎㈃_?덉쇅瑜??섏쭊??")
    void throwException_whenCodeIsInvalid() {
        // given
        VerificationTarget verificationTarget = new VerificationTarget(VerificationType.EMAIL, TEST_EMAIL_1);
        VerificationMessage verificationMessage = new VerificationMessage(TEST_EMAIL_CODE, LocalDateTime.now().plusHours(1));
        Verification verification = Verification.issue(verificationTarget, verificationMessage);

        // then
        assertThatThrownBy(
                // when
                () -> verification.confirm("000000", LocalDateTime.now()))
                .isInstanceOf(DomainRuleException.class);
    }

    @Test
    @DisplayName("留뚮즺???몄쬆肄붾뱶瑜??뺤씤?섎㈃_?덉쇅瑜??섏쭊??")
    void throwException_whenVerificationExpired() {
        // given
        VerificationTarget verificationTarget = new VerificationTarget(VerificationType.EMAIL, TEST_EMAIL_1);
        VerificationMessage verificationMessage = new VerificationMessage(TEST_EMAIL_CODE, LocalDateTime.now().minusMinutes(1));
        Verification verification = Verification.issue(verificationTarget, verificationMessage);

        // then
        assertThatThrownBy(
                // when
                () -> verification.confirm(TEST_EMAIL_CODE, LocalDateTime.now()))
                .isInstanceOf(DomainRuleException.class);
    }

    @Test
    @DisplayName("?대?_?몄쬆???곹깭?먯꽌_?ㅼ떆_?몄쬆?대룄_?깃났?쒕떎")
    void confirmAgain_whenAlreadyVerified() {
        // given
        LocalDateTime now = LocalDateTime.now();
        VerificationTarget verificationTarget = new VerificationTarget(VerificationType.EMAIL, TEST_EMAIL_1);
        VerificationMessage verificationMessage = new VerificationMessage(TEST_EMAIL_CODE, LocalDateTime.now().plusMinutes(5));
        Verification verification = Verification.issue(verificationTarget, verificationMessage);

        // when
        verification.confirm(TEST_EMAIL_CODE, now.plusMinutes(1));

        // then
        assertThat(verification.isVerified()).isTrue();
    }

    @Test
    @DisplayName("?몄쬆?섍퀬_留뚮즺?섏?_?딆븯?ㅻ㈃_assertValid_???듦낵?쒕떎")
    void assertValid_success_whenVerifiedAndNotExpired() {
        // given
        LocalDateTime now = LocalDateTime.now();
        VerificationTarget verificationTarget = new VerificationTarget(VerificationType.EMAIL, TEST_EMAIL_1);
        VerificationMessage verificationMessage = new VerificationMessage(TEST_EMAIL_CODE, LocalDateTime.now().plusMinutes(5));
        Verification verification = Verification.issue(verificationTarget, verificationMessage);
        verification.confirm(TEST_EMAIL_CODE, now);

        // then
        assertThatCode(
                // when
                () -> verification.assertValid(now.plusMinutes(1)))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("誘몄씤利??곹깭?쇰㈃_assertValid_?먯꽌_?덉쇅瑜??섏쭊??")
    void throwException_whenAssertValidAndNotVerified() {
        // given
        VerificationTarget verificationTarget = new VerificationTarget(VerificationType.EMAIL, TEST_EMAIL_1);
        VerificationMessage verificationMessage = new VerificationMessage(TEST_EMAIL_CODE, LocalDateTime.now().plusMinutes(5));
        Verification verification = Verification.issue(verificationTarget, verificationMessage);

        // then
        assertThatThrownBy(
                // when
                () -> verification.assertValid(LocalDateTime.now()))
                .isInstanceOf(DomainRuleException.class);
    }

    @Test
    @DisplayName("?몄쬆?섏뿀?대룄_留뚮즺?섏뿀?ㅻ㈃_assertValid_?먯꽌_?덉쇅瑜??섏쭊??")
    void throwException_whenAssertValidAndExpired() {
        // given
        LocalDateTime now = LocalDateTime.now();
        VerificationTarget verificationTarget = new VerificationTarget(VerificationType.EMAIL, TEST_EMAIL_1);
        VerificationMessage verificationMessage = new VerificationMessage(TEST_EMAIL_CODE, LocalDateTime.now().plusMinutes(1));
        Verification verification = Verification.issue(verificationTarget, verificationMessage);

        verification.confirm(TEST_EMAIL_CODE, now);

        // then
        assertThatThrownBy(
                // when
                () -> verification.assertValid(now.plusMinutes(2)))
                .isInstanceOf(DomainRuleException.class);
    }
}
