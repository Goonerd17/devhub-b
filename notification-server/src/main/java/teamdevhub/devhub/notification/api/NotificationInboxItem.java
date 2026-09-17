package teamdevhub.devhub.notification.api;

public record NotificationInboxItem(String notificationGuid, String typeCd,
                                    String content, String registrationDate) {
}
