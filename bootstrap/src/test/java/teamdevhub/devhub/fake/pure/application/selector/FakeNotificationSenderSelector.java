package teamdevhub.devhub.fake.pure.application.selector;

import teamdevhub.devhub.notification.core.application.selector.NotificationSenderSelector;
import teamdevhub.devhub.notification.core.port.in.command.VerificationNotificationCommand;

public class FakeNotificationSenderSelector implements NotificationSenderSelector {

    private boolean sent = false;
    private VerificationNotificationCommand message;

    @Override
    public void sendVerification(VerificationNotificationCommand verificationNotificationCommand) {
        this.sent = true;
        this.message = verificationNotificationCommand;
    }

    public boolean isSent() {
        return sent;
    }

    public VerificationNotificationCommand getMessage() {
        return message;
    }
}
