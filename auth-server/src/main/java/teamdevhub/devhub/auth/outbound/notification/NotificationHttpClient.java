package teamdevhub.devhub.auth.outbound.notification;

import org.springframework.stereotype.Component;
import teamdevhub.devhub.auth.core.port.out.VerificationNotificationPort;

@Component
public class NotificationHttpClient implements VerificationNotificationPort {
    private final NotificationFeignClient client;

    public NotificationHttpClient(NotificationFeignClient client) {
        this.client = client;
    }

    @Override
    public void sendVerification(String target, String code) {
        client.sendVerification(new NotificationFeignClient.VerificationRequest(target, code));
    }
}
