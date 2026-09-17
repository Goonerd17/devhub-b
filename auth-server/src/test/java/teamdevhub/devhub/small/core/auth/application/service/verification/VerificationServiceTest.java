package teamdevhub.devhub.auth.core.auth.application.service.verification;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.auth.core.auth.application.selector.verification.VerificationIssuerSelector;
import teamdevhub.devhub.auth.core.auth.application.service.verification.VerificationService;
import teamdevhub.devhub.auth.core.auth.domain.Verification;
import teamdevhub.devhub.auth.core.auth.domain.vo.verification.VerificationTarget;
import teamdevhub.devhub.auth.core.auth.domain.vo.verification.VerificationType;
import teamdevhub.devhub.auth.core.auth.port.in.command.verification.ConfirmVerificationCommand;
import teamdevhub.devhub.auth.core.auth.port.in.command.verification.IssueVerificationCommand;
import teamdevhub.devhub.auth.core.auth.port.out.verification.VerificationIssuer;
import teamdevhub.devhub.shared.time.TimeProvider;
import teamdevhub.devhub.fake.pure.application.issuer.FakeEmailVerificationIssuer;
import teamdevhub.devhub.fake.pure.application.provider.FakeTimeProvider;
import teamdevhub.devhub.fake.pure.application.port.out.auth.verification.FakeVerificationRepository;
import teamdevhub.devhub.fake.pure.application.selector.FakeVerificationIssuerSelector;
import teamdevhub.devhub.shared.core.common.exception.BusinessRuleException;
import teamdevhub.devhub.shared.core.common.exception.DomainRuleException;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class VerificationServiceTest {

    private static final String TEST_EMAIL_1 = "user1@example.com";
    private static final String TEST_EMAIL_CODE = "123456";

    private VerificationService verificationService;

    private FakeVerificationRepository verificationRepository;

    @BeforeEach
    void init() {
        TimeProvider timeProvider = new FakeTimeProvider(LocalDateTime.of(2025, 1, 1, 12, 0));
        VerificationIssuer verificationIssuer = new FakeEmailVerificationIssuer(VerificationType.EMAIL, TEST_EMAIL_CODE, timeProvider);
        VerificationIssuerSelector issuerSelector = new FakeVerificationIssuerSelector(List.of(verificationIssuer));
        verificationRepository = new FakeVerificationRepository();

        verificationService = new VerificationService(
                timeProvider,
                issuerSelector,
                verificationRepository
        );
    }

    @Test
    @DisplayName("?몄쬆??諛쒓툒?섎㈃_??λ맂??")
    void issueVerificationSuccess() {
        // given
        VerificationTarget verificationTarget = VerificationTarget.of(VerificationType.EMAIL, TEST_EMAIL_1);

        IssueVerificationCommand issueVerificationCommand = new IssueVerificationCommand(verificationTarget);

        // when
        verificationService.issueVerification(issueVerificationCommand);

        // then
        Verification verification = verificationRepository.findByVerificationTarget(verificationTarget);

        assertThat(verification).isNotNull();
    }

    @Test
    @DisplayName("?щ컮瑜??몄쬆肄붾뱶濡??몄쬆_?뺤씤???깃났?쒕떎")
    void confirmVerificationSuccess() {
        // given
        VerificationTarget verificationTarget = VerificationTarget.of(VerificationType.EMAIL, TEST_EMAIL_1);
        verificationService.issueVerification(new IssueVerificationCommand(verificationTarget));

        // when
        verificationService.confirmVerification(new ConfirmVerificationCommand(verificationTarget, TEST_EMAIL_CODE));

        // then
        Verification verification = verificationRepository.findByVerificationTarget(verificationTarget);

        assertThat(verification.isVerified()).isTrue();
    }

    @Test
    @DisplayName("?몄쬆?섏?_?딆?_?곹깭?먯꽌_assertAllowed_?몄텧_???덉쇅媛_諛쒖깮?쒕떎")
    void assertAllowedFailWhenNotConfirmed() {
        // given
        VerificationTarget verificationTarget = VerificationTarget.of(VerificationType.EMAIL, TEST_EMAIL_1);

        verificationService.issueVerification(new IssueVerificationCommand(verificationTarget));

        // when & then
        assertThatThrownBy(
                () -> verificationService.assertAllowed(verificationTarget))
                .isInstanceOf(DomainRuleException.class);
    }

    @Test
    @DisplayName("?몄쬆_?꾨즺_??assertAllowed_?듦낵?쒕떎")
    void assertAllowedSuccess() {
        // given
        VerificationTarget verificationTarget = VerificationTarget.of(VerificationType.EMAIL, TEST_EMAIL_1);

        verificationService.issueVerification(new IssueVerificationCommand(verificationTarget));

        verificationService.confirmVerification(new ConfirmVerificationCommand(verificationTarget, TEST_EMAIL_CODE));

        // when & then
        assertThatCode(
                () -> verificationService.assertAllowed(verificationTarget))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("consume ?몄텧 ???몄쬆 ?뺣낫媛 ??젣?쒕떎")
    void consume_success() {
        // given
        VerificationTarget target = VerificationTarget.of(VerificationType.EMAIL, TEST_EMAIL_1);

        verificationService.issueVerification(new IssueVerificationCommand(target));

        // when
        verificationService.consume(target);

        // then
        assertThatThrownBy(
                () -> verificationRepository.findByVerificationTarget(target))
                .isInstanceOf(BusinessRuleException.class);
    }
}
