package teamdevhub.devhub.notification.core.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import teamdevhub.devhub.notification.core.domain.Notification;
import teamdevhub.devhub.notification.core.port.in.NotificationQueryUseCase;
import teamdevhub.devhub.notification.core.port.out.NotificationQueryRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationQueryService implements NotificationQueryUseCase {

    private final NotificationQueryRepository notificationQueryRepository;

    @Override
    public List<Notification> getNotificationList(String userGuid) {
        return notificationQueryRepository.getNotificationList(userGuid);
    }
}
