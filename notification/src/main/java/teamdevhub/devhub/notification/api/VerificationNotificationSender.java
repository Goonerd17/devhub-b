package teamdevhub.devhub.notification.api;

/** Identity가 인증 코드를 전달하기 위해 사용하는 Notification 공개 계약. */
public interface VerificationNotificationSender {
    void sendVerification(String target, String code);
}
