package teamdevhub.devhub.notification.api;

import java.util.List;

/** The notification capability exposed to the HTTP inbox adapter. */
public interface NotificationInboxQuery {
    List<NotificationInboxItem> getNotificationList(String userGuid);

    void checkedNotification(String notificationGuid);
}
