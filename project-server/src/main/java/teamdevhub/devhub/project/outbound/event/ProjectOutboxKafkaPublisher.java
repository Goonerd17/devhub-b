package teamdevhub.devhub.project.outbound.event;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.ProducerRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import teamdevhub.devhub.project.outbound.event.persistence.ProjectOutboxEventEntity;
import teamdevhub.devhub.project.outbound.event.persistence.ProjectOutboxEventRepository;
import teamdevhub.devhub.shared.event.IntegrationEvent;

import java.time.Instant;
import java.time.Duration;

@Component
@RequiredArgsConstructor
public class ProjectOutboxKafkaPublisher {
    private final ProjectOutboxEventRepository repository;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Value("${events.kafka.topic:devhub.project.events}")
    private String topic;

    @Value("${events.outbox.retention-days:7}")
    private long retentionDays;

    @Scheduled(fixedDelayString = "${events.outbox.poll-interval-ms:1000}")
    @Transactional
    public void publishPendingEvents() {
        for (ProjectOutboxEventEntity event : repository.findTop100ByPublishedAtIsNullOrderByOccurredAtAsc()) {
            try {
                JsonNode payload = objectMapper.readTree(event.getPayload());
                IntegrationEvent envelope = new IntegrationEvent(
                        event.getEventId(), event.getEventType(), event.getSchemaVersion(), event.getOccurredAt(),
                        event.getAggregateType(), event.getAggregateId(), event.getCorrelationId(),
                        event.getCausationId(), payload);
                ProducerRecord<String, String> record = new ProducerRecord<>(
                        topic, event.getAggregateId(), objectMapper.writeValueAsString(envelope));
                addHeader(record, "eventId", event.getEventId());
                addHeader(record, "eventType", event.getEventType());
                addHeader(record, "correlationId", event.getCorrelationId());
                addHeader(record, "causationId", event.getCausationId());
                kafkaTemplate.send(record).get();
                event.markPublished(Instant.now());
            } catch (Exception exception) {
                event.markFailed(exception.getMessage());
            }
        }
    }

    @Scheduled(fixedDelayString = "${events.outbox.cleanup-interval-ms:3600000}")
    @Transactional
    public void cleanupPublishedEvents() {
        Instant cutoff = Instant.now().minus(Duration.ofDays(retentionDays));
        repository.deleteByPublishedAtBefore(cutoff);
    }

    private static void addHeader(ProducerRecord<String, String> record, String name, String value) {
        if (value != null && !value.isBlank()) {
            record.headers().add(name, value.getBytes(java.nio.charset.StandardCharsets.UTF_8));
        }
    }
}
