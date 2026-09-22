package teamdevhub.devhub.shared.notification;

public interface VerificationNotificationSender {
    void sendVerification(String target, String code);
}
