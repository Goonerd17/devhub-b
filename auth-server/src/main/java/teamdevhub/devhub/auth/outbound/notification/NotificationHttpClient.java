package teamdevhub.devhub.auth.outbound.notification;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import teamdevhub.devhub.shared.notification.VerificationNotificationSender;

@Component
public class NotificationHttpClient implements VerificationNotificationSender {
    private final RestClient client;

    public NotificationHttpClient(RestClient.Builder builder,
            @Value("${services.notification.base-url:http://notification-server}") String baseUrl) {
        this.client = builder.baseUrl(baseUrl).build();
    }

    @Override
    public void sendVerification(String target, String code) {
        client.post()
                .uri("/internal/notifications/verifications")
                .body(new VerificationRequest(target, code))
                .retrieve()
                .toBodilessEntity();
    }

    private record VerificationRequest(String target, String code) {
    }
}
