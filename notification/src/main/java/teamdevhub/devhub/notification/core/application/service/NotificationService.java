package teamdevhub.devhub.notification.core.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import teamdevhub.devhub.notification.core.application.selector.NotificationSenderSelector;
import teamdevhub.devhub.notification.core.domain.Notification;
import teamdevhub.devhub.notification.core.port.in.NotificationUseCase;
import teamdevhub.devhub.notification.core.port.in.command.CreateNotificationCommand;
import teamdevhub.devhub.notification.core.port.in.command.VerificationNotificationCommand;
import teamdevhub.devhub.notification.api.VerificationNotificationSender;
import teamdevhub.devhub.notification.core.port.out.NotificationRepository;

@Service
@RequiredArgsConstructor
public class NotificationService implements NotificationUseCase, VerificationNotificationSender {

    @Override
    public void sendVerification(String target, String code) {
        sendVerification(new VerificationNotificationCommand(target, code));
    }

    private final NotificationSenderSelector notificationSenderSelector;
    private final NotificationRepository notificationRepository;

    @Override
    public void sendVerification(VerificationNotificationCommand verificationNotificationCommand) {
        notificationSenderSelector.sendVerification(verificationNotificationCommand);
    }

    @Override
    public void checkedNotification(String notificationGuid) {
        Notification notification = notificationRepository.getNotification(notificationGuid);
        notification.checked();
        notificationRepository.save(notification);
    }

    @Override
    public void createNotification(CreateNotificationCommand notificationCommand) {
        notificationRepository.save(Notification.create(notificationCommand));
    }
}
