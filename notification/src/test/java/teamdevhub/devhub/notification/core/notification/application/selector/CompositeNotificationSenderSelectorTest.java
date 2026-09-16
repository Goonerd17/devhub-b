package teamdevhub.devhub.notification.core.notification.application.selector;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import teamdevhub.devhub.notification.core.application.selector.CompositeMessageSenderSelector;
import teamdevhub.devhub.platform.outbound.common.exception.ExternalServiceException;
import teamdevhub.devhub.notification.core.port.out.NotificationSender;
import teamdevhub.devhub.notification.core.port.in.command.VerificationNotificationCommand;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class CompositeNotificationSenderSelectorTest {

    private static final String TEST_EMAIL = "test@test.com";
    private static final String TEST_EMAIL_CODE = "123456";

    private CompositeMessageSenderSelector compositeMessageSenderSelector;
    private FakeNotificationSender emailSender;


    static class FakeNotificationSender implements NotificationSender {
        private boolean sent = false;
        private VerificationNotificationCommand lastMessage;

        @Override
        public boolean supports(VerificationNotificationCommand verificationNotificationCommand) {
            return verificationNotificationCommand.target().contains("@");
        }

        @Override
        public void sendVerification(VerificationNotificationCommand verificationNotificationCommand) {
            this.sent = true;
            this.lastMessage = verificationNotificationCommand;
        }

        public boolean isSent() {
            return sent;
        }

        public VerificationNotificationCommand getLastMessage() {
            return lastMessage;
        }
    }

    @BeforeEach
    void init() {
        emailSender = new FakeNotificationSender();
        FakeNotificationSender smsSender = new FakeNotificationSender();
        compositeMessageSenderSelector = new CompositeMessageSenderSelector(List.of(emailSender, smsSender));
    }

    @Test
    @DisplayName("지원되는_VerificationTarget_이면_해당_MessageSender_가_호출된다")
    void sendVerificationCallsCorrectSender() {
        // given
        VerificationNotificationCommand verificationCommand = new VerificationNotificationCommand(TEST_EMAIL, TEST_EMAIL_CODE);

        // when
        compositeMessageSenderSelector.sendVerification(verificationCommand);

        // then
        assertThat(emailSender.isSent()).isTrue();
        assertThat(emailSender.getLastMessage()).isEqualTo(verificationCommand);
    }

    @Test
    @DisplayName("지원하지_않는_VerificationTarget_이면_예외가_발생한다")
    void unsupportedVerificationTargetThrows() {
        // given
        VerificationNotificationCommand unsupportedCommand = new VerificationNotificationCommand("123456", "123456");

        // when, then
        assertThatThrownBy(() -> compositeMessageSenderSelector.sendVerification(unsupportedCommand))
                .isInstanceOf(ExternalServiceException.class)
                .hasMessageContaining("발송이 실패했습니다");
    }
}
