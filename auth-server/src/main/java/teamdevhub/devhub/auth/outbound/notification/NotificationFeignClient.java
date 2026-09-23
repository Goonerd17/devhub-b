package teamdevhub.devhub.auth.outbound.notification;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import teamdevhub.devhub.shared.internal.InternalFeignConfiguration;

@FeignClient(name = "notification-server", configuration = InternalFeignConfiguration.class)
public interface NotificationFeignClient {
    @PostMapping("/internal/notifications/verifications")
    void sendVerification(@RequestBody VerificationRequest request);

    record VerificationRequest(String target, String code) { }
}
