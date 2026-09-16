package teamdevhub.devhub.identity.core.auth.port.facade;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.identity.core.auth.domain.vo.verification.VerificationType;
import teamdevhub.devhub.identity.core.auth.port.in.command.verification.ConfirmVerificationCommand;
import teamdevhub.devhub.identity.core.auth.port.in.command.verification.IssueVerificationCommand;
import teamdevhub.devhub.identity.core.auth.port.in.facade.VerificationFacade;
import teamdevhub.devhub.identity.core.auth.domain.vo.verification.VerificationTarget;
import teamdevhub.devhub.fake.pure.application.port.in.usecase.auth.verification.FakeVerificationUseCase;
import teamdevhub.devhub.fake.pure.application.port.in.usecase.notification.FakeNotificationUseCase;

import static org.assertj.core.api.Assertions.assertThat;
import static teamdevhub.devhub.constant.UserTestConstant.*;

public class VerificationFacadeTest {

    private VerificationFacade verificationFacade;

    private FakeVerificationUseCase verificationUseCase;
    private FakeNotificationUseCase notificationUseCase;

    @BeforeEach
    void init() {
        verificationUseCase = new FakeVerificationUseCase();
        notificationUseCase = new FakeNotificationUseCase();

        verificationFacade = new VerificationFacade(verificationUseCase, notificationUseCase);
    }

    @Test
    @DisplayName("issueVerification_?_?몄쬆??諛쒓툒?섍퀬_?뚮┝???꾩넚?쒕떎")
    void issueVerification_issuesAndSendsNotification() {
        // given
        VerificationTarget target = VerificationTarget.of(VerificationType.EMAIL, TEST_EMAIL_1);
        IssueVerificationCommand issueVerificationCommand = new IssueVerificationCommand(target);

        // when
        verificationFacade.issueVerification(issueVerificationCommand);

        // then
        assertThat(notificationUseCase.getSentVerifications()).hasSize(1);
    }

    @Test
    @DisplayName("confirmVerification_?_?몄쬆_肄붾뱶瑜?寃利앺븳??")
    void confirmVerification_confirmsCode() {
        // given
        VerificationTarget target = VerificationTarget.of(VerificationType.EMAIL, TEST_EMAIL_1);
        IssueVerificationCommand issueCommand = new IssueVerificationCommand(target);
        verificationFacade.issueVerification(issueCommand);

        ConfirmVerificationCommand confirmCommand = new ConfirmVerificationCommand(target, TEST_EMAIL_CODE);

        // when, then
        verificationFacade.confirmVerification(confirmCommand);
    }
}
