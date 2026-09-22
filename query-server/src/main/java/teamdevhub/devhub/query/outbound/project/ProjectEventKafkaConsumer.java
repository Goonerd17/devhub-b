package teamdevhub.devhub.query.outbound.project;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import teamdevhub.devhub.query.outbound.project.persistence.ProjectProjectionEntity;
import teamdevhub.devhub.query.outbound.project.persistence.ProjectProjectionRepository;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class ProjectEventKafkaConsumer {
    private final ObjectMapper objectMapper;
    private final ProjectProjectionRepository repository;

    @KafkaListener(topics = "${events.kafka.topic:devhub.project.events}",
            groupId = "${spring.kafka.consumer.group-id:query-project-projection}")
    @Transactional
    public void consume(String message) {
        try {
            JsonNode event = objectMapper.readTree(message);
            assertSupportedSchema(event);
            String eventId = event.path("eventId").asText();
            if (eventId.isBlank() || repository.existsByLastEventId(eventId)) {
                return;
            }
            String projectGuid = event.path("aggregateId").asText();
            Instant eventAt = Instant.parse(event.path("occurredAt").asText());
            String eventType = event.path("eventType").asText();
            ProjectProjectionEntity projection = repository.findById(projectGuid).orElse(null);
            switch (eventType) {
                case "project.created" -> repository.save(ProjectProjectionEntity.created(
                        projectGuid,
                        event.path("payload").path("ownerGuid").asText(null),
                        event.path("payload").path("title").asText(null),
                        event.path("payload").path("category").asText(null),
                        event.path("payload").path("username").asText(null),
                        event.path("payload").path("imageFileGuid").asText(null),
                        date(event.path("payload").path("recruitmentStartDate")),
                        date(event.path("payload").path("recruitmentEndDate")),
                        dateTime(event.path("payload").path("registeredDate")),
                        event.path("payload").path("capacityClosed").asBoolean(false),
                        event.path("payload").path("recruitStatus").asText(null),
                        eventId,
                        eventAt));
                case "project.updated" -> {
                    if (projection != null) {
                        JsonNode payload = event.path("payload");
                        projection.applyUpdate(payload.path("title").asText(null), payload.path("category").asText(null),
                                payload.path("username").asText(null), payload.path("imageFileGuid").asText(null),
                                date(payload.path("recruitmentStartDate")), date(payload.path("recruitmentEndDate")),
                                payload.path("capacityClosed").asBoolean(false), payload.path("recruitStatus").asText(null),
                                eventId, eventAt);
                    }
                }
                case "project.closed" -> {
                    if (projection != null) {
                        projection.applyClosed(eventId, eventAt);
                    }
                }
                default -> { return; }
            }
            if (projection != null && !"project.created".equals(eventType)) {
                repository.save(projection);
            }
        } catch (Exception exception) {
            throw new IllegalStateException("Unable to consume project event", exception);
        }
    }

    private static LocalDate date(JsonNode node) {
        return node.isMissingNode() || node.isNull() || node.asText().isBlank() ? null : LocalDate.parse(node.asText());
    }

    private static LocalDateTime dateTime(JsonNode node) {
        return node.isMissingNode() || node.isNull() || node.asText().isBlank() ? null : LocalDateTime.parse(node.asText());
    }

    private static void assertSupportedSchema(JsonNode event) {
        if (event.path("schemaVersion").asInt(0) != 1) {
            throw new IllegalArgumentException("Unsupported project event schema version");
        }
    }
}
