package teamdevhub.devhub.notification.outbound.notification.adapter;

import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mail.javamail.JavaMailSender;
import org.thymeleaf.spring6.SpringTemplateEngine;
import teamdevhub.devhub.platform.outbound.common.exception.ExternalServiceException;
import teamdevhub.devhub.notification.outbound.adapter.EmailNotificationSendAdapter;
import teamdevhub.devhub.notification.core.port.in.command.VerificationNotificationCommand;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

public class EmailMessageSendAdapterTest {

    private static final String TEST_EMAIL = "test@test.com";
    private static final String TEST_EMAIL_CODE = "123456";

    private EmailNotificationSendAdapter emailMessageSendAdapter;

    private JavaMailSender mailSender;
    private SpringTemplateEngine templateEngine;

    @BeforeEach
    void init() {
        mailSender = mock(JavaMailSender.class);
        templateEngine = mock(SpringTemplateEngine.class);

        emailMessageSendAdapter = new EmailNotificationSendAdapter(mailSender, templateEngine);
    }

    @Test
    @DisplayName("VerificationType_이_EMAIL_이면_supports_는_true_를_반환한다")
    void supportsReturnsTrueForEmail() {
        VerificationNotificationCommand verificationTarget = new VerificationNotificationCommand(TEST_EMAIL, TEST_EMAIL_CODE);

        assertThat(emailMessageSendAdapter.supports(verificationTarget)).isTrue();
    }

    @Test
    @DisplayName("VerificationType_이_SMS_이면_supports_는_false_를_반환한다")
    void supportsReturnsFalseForSMS() {
        VerificationNotificationCommand verificationTarget = new VerificationNotificationCommand("01012345678", TEST_EMAIL_CODE);

        assertThat(emailMessageSendAdapter.supports(verificationTarget)).isFalse();
    }

    @Test
    @DisplayName("메일_전송_호출시_JavaMailSender_가_호출된다")
    void sendVerificationCallsMailSender() {
        // given
        VerificationNotificationCommand verificationCommand = new VerificationNotificationCommand(TEST_EMAIL, TEST_EMAIL_CODE);

        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        when(templateEngine.process(anyString(), any())).thenReturn("<html>test</html>");

        // when
        emailMessageSendAdapter.sendVerification(verificationCommand);

        // then
        verify(mailSender, times(1)).send(mimeMessage);
        verify(templateEngine, times(1)).process(anyString(), any());
    }

    @Test
    @DisplayName("지원하지_않는_VerificationType_으로_send_하면_예외가_발생한다")
    void sendVerificationThrowsExternalServiceException() {
        // given
        VerificationNotificationCommand command = new VerificationNotificationCommand("010-1234-5678", "123456");

        // when, then
        assertThatThrownBy(() -> emailMessageSendAdapter.sendVerification(command))
                .isInstanceOf(ExternalServiceException.class)
                .hasMessageContaining("발송이 실패했습니다");
    }
}
