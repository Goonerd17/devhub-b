package teamdevhub.devhub.notification.core.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import teamdevhub.devhub.notification.api.NotificationInboxItem;
import teamdevhub.devhub.notification.api.NotificationInboxQuery;
import teamdevhub.devhub.notification.core.port.in.NotificationQueryUseCase;
import teamdevhub.devhub.notification.core.port.in.NotificationUseCase;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationInboxService implements NotificationInboxQuery {
    private final NotificationQueryUseCase notificationQueryUseCase;
    private final NotificationUseCase notificationUseCase;

    @Override
    public List<NotificationInboxItem> getNotificationList(String userGuid) {
        return notificationQueryUseCase.getNotificationList(userGuid).stream()
                .map(notification -> new NotificationInboxItem(
                        notification.getNotificationGuid(), notification.getTypeCd(),
                        notification.getContent(), notification.getAuditInfo().registrantGuid()))
                .toList();
    }

    @Override
    public void checkedNotification(String notificationGuid) {
        notificationUseCase.checkedNotification(notificationGuid);
    }
}
