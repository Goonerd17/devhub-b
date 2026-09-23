package teamdevhub.devhub.auth.core.port.out;

public interface VerificationNotificationPort {
    void sendVerification(String target, String code);
}
