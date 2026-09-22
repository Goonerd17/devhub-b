package teamdevhub.devhub.project.outbound.event.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "project_outbox_event")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProjectOutboxEventEntity {
    @Id
    private String eventId;

    @Column(nullable = false, length = 120)
    private String eventType;

    @Column(nullable = false)
    private int schemaVersion;

    @Column(nullable = false)
    private Instant occurredAt;

    private Instant publishedAt;

    @Column(nullable = false)
    private int publishAttempts;

    @Column(length = 1000)
    private String lastError;

    @Column(nullable = false, length = 80)
    private String aggregateType;

    @Column(nullable = false, length = 120)
    private String aggregateId;

    @Column(length = 120)
    private String correlationId;

    @Column(length = 120)
    private String causationId;

    @Lob
    @Column(nullable = false)
    private String payload;

    private ProjectOutboxEventEntity(String eventId, String eventType, int schemaVersion,
            Instant occurredAt, String aggregateType, String aggregateId,
            String correlationId, String causationId, String payload) {
        this.eventId = eventId;
        this.eventType = eventType;
        this.schemaVersion = schemaVersion;
        this.occurredAt = occurredAt;
        this.publishAttempts = 0;
        this.aggregateType = aggregateType;
        this.aggregateId = aggregateId;
        this.correlationId = correlationId;
        this.causationId = causationId;
        this.payload = payload;
    }

    public void markPublished(Instant publishedAt) {
        this.publishedAt = publishedAt;
        this.lastError = null;
    }

    public void markFailed(String error) {
        this.publishAttempts++;
        this.lastError = error == null ? "unknown error" : error.substring(0, Math.min(error.length(), 1000));
    }

    public static ProjectOutboxEventEntity from(
            teamdevhub.devhub.shared.event.IntegrationEvent event, String payload) {
        return new ProjectOutboxEventEntity(
                event.eventId(), event.eventType(), event.schemaVersion(), event.occurredAt(),
                event.aggregateType(), event.aggregateId(), event.correlationId(),
                event.causationId(), payload);
    }
}
