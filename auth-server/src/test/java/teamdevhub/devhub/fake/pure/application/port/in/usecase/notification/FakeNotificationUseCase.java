package teamdevhub.devhub.fake.pure.application.port.in.usecase.notification;

import teamdevhub.devhub.auth.core.port.out.VerificationNotificationPort;

import java.util.ArrayList;
import java.util.List;

public class FakeNotificationUseCase implements VerificationNotificationPort {

    private final List<String> sentVerifications = new ArrayList<>();

    public void sendVerification(String target, String code) {
        sentVerifications.add(target + ":" + code);
    }

    public List<String> getSentVerifications() {
        return sentVerifications;
    }
}
