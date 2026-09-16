package teamdevhub.devhub.notification.core.application.selector;

import teamdevhub.devhub.notification.core.port.in.command.VerificationNotificationCommand;

public interface NotificationSenderSelector {

    void sendVerification(VerificationNotificationCommand verificationNotificationCommand);
}
