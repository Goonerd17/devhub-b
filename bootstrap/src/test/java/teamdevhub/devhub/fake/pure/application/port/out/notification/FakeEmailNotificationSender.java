package teamdevhub.devhub.fake.pure.application.port.out.notification;

import teamdevhub.devhub.notification.core.port.out.NotificationSender;
import teamdevhub.devhub.notification.core.port.in.command.VerificationNotificationCommand;

import java.util.ArrayList;
import java.util.List;

public class FakeEmailNotificationSender implements NotificationSender {

    private final List<String> sentEmails = new ArrayList<>();

    @Override
    public boolean supports(VerificationNotificationCommand verificationNotificationCommand) {
        return verificationNotificationCommand.target().contains("@");
    }

    @Override
    public void sendVerification(VerificationNotificationCommand verificationNotificationCommand) {
        sentEmails.add(verificationNotificationCommand.target());
    }

    public boolean wasSentTo(String email) {
        return sentEmails.contains(email);
    }
}
