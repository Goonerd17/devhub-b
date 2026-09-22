package teamdevhub.devhub.notification.outbound.event;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "notification_processed_event")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
class ProcessedNotificationEventEntity {
    @Id
    private String eventId;
    private Instant processedAt;

    ProcessedNotificationEventEntity(String eventId, Instant processedAt) {
        this.eventId = eventId;
        this.processedAt = processedAt;
    }
}
