package teamdevhub.devhub.notification.core.port.out;

import teamdevhub.devhub.notification.core.port.in.command.VerificationNotificationCommand;

public interface NotificationSender {

    boolean supports(VerificationNotificationCommand verificationNotificationCommand);
    void sendVerification(VerificationNotificationCommand verificationNotificationCommand);
}
