package teamdevhub.devhub.notification.core.port.out;

import teamdevhub.devhub.notification.core.domain.Notification;

import java.util.List;

public interface NotificationQueryRepository {

    List<Notification> getNotificationList(String userGuid);
}
