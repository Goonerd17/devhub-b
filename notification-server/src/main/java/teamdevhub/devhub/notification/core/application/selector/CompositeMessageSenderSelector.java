package teamdevhub.devhub.notification.core.application.selector;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import teamdevhub.devhub.notification.core.port.out.NotificationSender;
import teamdevhub.devhub.notification.core.port.in.command.VerificationNotificationCommand;
import teamdevhub.devhub.shared.outbound.common.exception.ExternalServiceException;
import teamdevhub.devhub.shared.enums.ErrorCode;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CompositeMessageSenderSelector implements NotificationSenderSelector {

    private final List<NotificationSender> notificationSenderList;

    public void sendVerification(VerificationNotificationCommand verificationNotificationCommand) {
        findMessageSender(verificationNotificationCommand)
                .sendVerification(verificationNotificationCommand);
    }

    private NotificationSender findMessageSender(VerificationNotificationCommand verificationNotificationCommand) {
        return notificationSenderList.stream()
                .filter(sender -> sender.supports(verificationNotificationCommand))
                .findFirst()
                .orElseThrow(
                        () -> ExternalServiceException.of(ErrorCode.NOTIFICATION_SEND_FAIL));
    }
}
