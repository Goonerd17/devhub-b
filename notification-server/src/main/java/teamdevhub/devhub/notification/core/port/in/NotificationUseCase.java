package teamdevhub.devhub.notification.core.port.in;

import teamdevhub.devhub.notification.core.port.in.command.CreateNotificationCommand;
import teamdevhub.devhub.notification.core.port.in.command.VerificationNotificationCommand;

public interface NotificationUseCase {

    void sendVerification(VerificationNotificationCommand verificationNotificationCommand);

    void checkedNotification(String notificationGuid);

    void createNotification(CreateNotificationCommand notificationCommand);
}
