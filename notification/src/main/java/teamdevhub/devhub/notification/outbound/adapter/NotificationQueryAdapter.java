package teamdevhub.devhub.notification.outbound.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import teamdevhub.devhub.notification.core.domain.Notification;
import teamdevhub.devhub.notification.core.port.out.NotificationQueryRepository;
import teamdevhub.devhub.notification.outbound.adapter.mapper.NotificationMapper;
import teamdevhub.devhub.notification.outbound.persistence.JpaNotificationRepository;

import java.util.List;

@Component
@RequiredArgsConstructor
public class NotificationQueryAdapter implements NotificationQueryRepository {

    private final JpaNotificationRepository jpaNotificationRepository;

    @Override
    public List<Notification> getNotificationList(String userGuid) {
        return jpaNotificationRepository.findAllByReceiverAndCheckedFalse(userGuid).stream()
                .map(NotificationMapper::toDomain)
                .toList();
    }
}
