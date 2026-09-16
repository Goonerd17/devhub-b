package teamdevhub.devhub.notification.core.port.out;

import teamdevhub.devhub.notification.core.domain.Notification;

public interface NotificationRepository {

    Notification getNotification(String notificationGuid);

    void save(Notification notification);
}
