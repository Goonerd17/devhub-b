package teamdevhub.devhub.notification.core.port.in.command;

/**
 * Identity가 검증 메시지 전달을 요청할 때 사용하는 Notification의 의미적 입력값이다.
 * 인증 도메인의 Verification aggregate를 Notification에 노출하지 않는다.
 */
public record VerificationNotificationCommand(String target, String code) {
}
