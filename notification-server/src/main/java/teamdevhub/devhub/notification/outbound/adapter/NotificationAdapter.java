package teamdevhub.devhub.notification.outbound.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import teamdevhub.devhub.notification.core.domain.Notification;
import teamdevhub.devhub.notification.core.port.out.NotificationRepository;
import teamdevhub.devhub.notification.outbound.adapter.mapper.NotificationMapper;
import teamdevhub.devhub.notification.outbound.persistence.JpaNotificationRepository;

@Component
@RequiredArgsConstructor
public class NotificationAdapter implements NotificationRepository {

    private final JpaNotificationRepository jpaNotificationRepository;

    @Override
    public Notification getNotification(String notificationGuid) {
        return NotificationMapper.toDomain(jpaNotificationRepository.findAllByNotificationGuid(notificationGuid));
    }

    @Override
    public void save(Notification notification) {
        jpaNotificationRepository.save(NotificationMapper.toEntity(notification));
    }
}
