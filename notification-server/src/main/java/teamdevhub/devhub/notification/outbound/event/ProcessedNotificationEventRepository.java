package teamdevhub.devhub.notification.outbound.event;

import org.springframework.data.jpa.repository.JpaRepository;

interface ProcessedNotificationEventRepository extends JpaRepository<ProcessedNotificationEventEntity, String> {
}
