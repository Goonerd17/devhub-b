package teamdevhub.devhub.fake.pure.application.port.in.usecase.notification;

import teamdevhub.devhub.notification.core.port.in.NotificationUseCase;
import teamdevhub.devhub.notification.core.port.in.command.CreateNotificationCommand;
import teamdevhub.devhub.notification.core.port.in.command.VerificationNotificationCommand;
import teamdevhub.devhub.shared.notification.VerificationNotificationSender;

import java.util.ArrayList;
import java.util.List;

public class FakeNotificationUseCase implements NotificationUseCase, VerificationNotificationSender {

    private final List<VerificationNotificationCommand> sentVerifications = new ArrayList<>();

    @Override
    public void sendVerification(VerificationNotificationCommand verificationNotificationCommand) {
        sentVerifications.add(verificationNotificationCommand);
    }

    @Override
    public void sendVerification(String target, String code) {
        sendVerification(new VerificationNotificationCommand(target, code));
    }

    @Override
    public void checkedNotification(String notificationGuid) {
    }

    @Override
    public void createNotification(CreateNotificationCommand notificationCommand) {
    }

    public List<VerificationNotificationCommand> getSentVerifications() {
        return sentVerifications;
    }
}
