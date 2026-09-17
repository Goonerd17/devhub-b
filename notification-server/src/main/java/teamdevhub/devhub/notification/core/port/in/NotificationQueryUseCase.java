package teamdevhub.devhub.notification.core.port.in;

import teamdevhub.devhub.notification.core.domain.Notification;

import java.util.List;

public interface NotificationQueryUseCase {

    List<Notification> getNotificationList(String userGuid);
}
