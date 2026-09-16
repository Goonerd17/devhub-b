package teamdevhub.devhub.notification.outbound.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import teamdevhub.devhub.notification.outbound.adapter.entity.NotificationEntity;

import java.util.List;

public interface JpaNotificationRepository extends JpaRepository<NotificationEntity, String> {

    List<NotificationEntity> findAllByReceiverAndCheckedFalse(String userGuid);

    NotificationEntity findAllByNotificationGuid(String notificationGuid);
}
