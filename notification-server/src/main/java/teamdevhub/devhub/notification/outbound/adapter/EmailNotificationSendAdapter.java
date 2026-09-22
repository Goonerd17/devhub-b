package teamdevhub.devhub.notification.outbound.adapter;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import teamdevhub.devhub.shared.outbound.common.exception.ExternalServiceException;
import teamdevhub.devhub.shared.enums.ErrorCode;
import teamdevhub.devhub.notification.core.port.in.command.VerificationNotificationCommand;
import teamdevhub.devhub.notification.core.port.out.NotificationSender;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class EmailNotificationSendAdapter implements NotificationSender {

    private static final String ENCODING = "UTF-8";

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    @Override
    public boolean supports(VerificationNotificationCommand verificationNotificationCommand) {
        return verificationNotificationCommand.target().contains("@");
    }

    @Override
    public void sendVerification(VerificationNotificationCommand verificationNotificationCommand) {
        EmailTemplateType emailTemplateType = EmailTemplateType.EMAIL_VERIFICATION;
        Map<String, Object> variables = toVariables(emailTemplateType, verificationNotificationCommand);
        send(verificationNotificationCommand.target(), emailTemplateType, variables);
    }

    private Map<String, Object> toVariables(EmailTemplateType template, VerificationNotificationCommand message) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("code", message.code());
        variables.put("expireTime", template.getExpireTime());
        return variables;
    }

    private void send(String email, EmailTemplateType template, Map<String, Object> variables) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, ENCODING);

            helper.setTo(email);
            helper.setSubject(template.getSubject());
            helper.setText(buildBody(template, variables), true);

            mailSender.send(message);
        } catch (Exception e) {
            throw ExternalServiceException.of(ErrorCode.NOTIFICATION_SEND_FAIL);
        }
    }

    private String buildBody(EmailTemplateType template, Map<String, Object> variables) {
        Context context = new Context();
        context.setVariables(variables);
        return templateEngine.process(template.getPath(), context);
    }
}
