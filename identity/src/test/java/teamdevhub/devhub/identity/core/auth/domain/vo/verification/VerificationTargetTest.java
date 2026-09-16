package teamdevhub.devhub.identity.core.auth.domain.vo.verification;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.platform.shared.enums.ErrorCode;
import teamdevhub.devhub.platform.core.common.exception.DomainRuleException;
import teamdevhub.devhub.identity.core.auth.domain.vo.verification.VerificationTarget;
import teamdevhub.devhub.identity.core.auth.domain.vo.verification.VerificationType;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class VerificationTargetTest {

    private static final String TEST_EMAIL = "test@test.com";

    @Test
    @DisplayName("EMAIL_??낃낵_?щ컮瑜??대찓??媛믪쑝濡?VerificationTarget_?앹꽦???깃났?쒕떎")
    void createVerificationTargetWithValidEmail() {
        VerificationTarget verificationTarget= VerificationTarget.of(VerificationType.EMAIL, TEST_EMAIL);

        assertThat(verificationTarget.verificationType()).isEqualTo(VerificationType.EMAIL);
        assertThat(verificationTarget.value()).isEqualTo(TEST_EMAIL);
    }

    @Test
    @DisplayName("EMAIL_??낃낵_?섎せ???대찓???뺤떇?대㈃_?덉쇅瑜??섏쭊??")
    void throwExceptionWhenInvalidEmailFormat() {
        assertThatThrownBy(() ->
                VerificationTarget.of(VerificationType.EMAIL, "invalid-email"))
                .isInstanceOf(DomainRuleException.class)
                .hasMessageContaining(ErrorCode.INVALID_EMAIL_FORMAT.getMessage());
    }

    @Test
    @DisplayName("SMS_??낃낵_?щ컮瑜??꾪솕踰덊샇_媛믪쑝濡?VerificationTarget_?앹꽦???깃났?쒕떎")
    void createVerificationTargetWithValidPhone() {
        VerificationTarget verificationTarget= VerificationTarget.of(VerificationType.SMS, "01012345678");

        assertThat(verificationTarget.verificationType()).isEqualTo(VerificationType.SMS);
        assertThat(verificationTarget.value()).isEqualTo("01012345678");
    }

    @Test
    @DisplayName("SMS_??낃낵_?섎せ???꾪솕踰덊샇_?뺤떇?대㈃_?덉쇅瑜??섏쭊??")
    void throwExceptionWhenInvalidPhoneFormat() {
        assertThatThrownBy(() ->
                VerificationTarget.of(VerificationType.SMS, "123456"))
                .isInstanceOf(DomainRuleException.class)
                .hasMessageContaining(ErrorCode.INVALID_PHONE_NUMBER_FORMAT.getMessage());
    }

    @Test
    @DisplayName("OTP_??낃낵_怨듬갚???꾨땶_媛믪쑝濡?VerificationTarget_?앹꽦???깃났?쒕떎")
    void createVerificationTargetWithValidOtp() {
        VerificationTarget verificationTarget= VerificationTarget.of(VerificationType.OTP, "123456");

        assertThat(verificationTarget.verificationType()).isEqualTo(VerificationType.OTP);
        assertThat(verificationTarget.value()).isEqualTo("123456");
    }

    @Test
    @DisplayName("OTP_??낃낵_怨듬갚_媛믪씠硫??덉쇅瑜??섏쭊??")
    void throwExceptionWhenOtpIsBlank() {
        assertThatThrownBy(() ->
                VerificationTarget.of(VerificationType.OTP, " "))
                .isInstanceOf(DomainRuleException.class)
                .hasMessageContaining(ErrorCode.OTP_BLANK.getMessage());
    }

    @Test
    @DisplayName("verificationType_??null_?대㈃_?덉쇅瑜??섏쭊??")
    void throwExceptionWhenVerificationTypeIsNull() {
        assertThatThrownBy(() ->
                VerificationTarget.of(null, "test@test.com"))
                .isInstanceOf(DomainRuleException.class)
                .hasMessageContaining(ErrorCode.VERIFICATION_TYPE_INVALID.getMessage());
    }

    @Test
    @DisplayName("value_媛_null_?대㈃_?덉쇅瑜??섏쭊??")
    void throwExceptionWhenValueIsNull() {
        assertThatThrownBy(() ->
                VerificationTarget.of(VerificationType.EMAIL, null))
                .isInstanceOf(DomainRuleException.class)
                .hasMessageContaining(ErrorCode.VERIFICATION_VALUE_REQUIRED.getMessage());
    }
}
