package teamdevhub.devhub.notification.http.internal;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import teamdevhub.devhub.notification.core.port.in.NotificationUseCase;
import teamdevhub.devhub.notification.core.port.in.command.VerificationNotificationCommand;

@RestController
@RequestMapping("/internal/notifications")
@RequiredArgsConstructor
public class NotificationInternalController {
    private final NotificationUseCase notificationUseCase;

    @PostMapping("/verifications")
    public ResponseEntity<Void> sendVerification(@RequestBody VerificationRequest request) {
        notificationUseCase.sendVerification(new VerificationNotificationCommand(request.target(), request.code()));
        return ResponseEntity.accepted().build();
    }

    public record VerificationRequest(String target, String code) {
    }
}
