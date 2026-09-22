package teamdevhub.devhub.query.outbound.application;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import teamdevhub.devhub.query.outbound.application.persistence.ProjectApplicationProjectionEntity;
import teamdevhub.devhub.query.outbound.application.persistence.ProjectApplicationProjectionRepository;
import teamdevhub.devhub.shared.internal.CorrelationIdFilter;

import java.time.Instant;

@Component
@RequiredArgsConstructor
public class ProjectApplicationEventKafkaConsumer {
    private final ObjectMapper objectMapper;
    private final ProjectApplicationProjectionRepository repository;

    @KafkaListener(topics = "${events.kafka.topic:devhub.project.events}",
            groupId = "${query.application.kafka.group-id:query-project-application-projection}")
    @Transactional
    public void consume(String message) {
        String correlationId = null;
        try {
            JsonNode event = objectMapper.readTree(message);
            correlationId = event.path("correlationId").asText(null);
            if (correlationId != null && !correlationId.isBlank()) {
                MDC.put(CorrelationIdFilter.MDC_KEY, correlationId);
            }
            if (event.path("schemaVersion").asInt(0) != 1) {
                throw new IllegalArgumentException("Unsupported project application event schema version");
            }
            String eventType = event.path("eventType").asText();
            if (!eventType.startsWith("project.application.")) {
                return;
            }
            String eventId = event.path("eventId").asText();
            if (eventId.isBlank() || repository.existsByLastEventId(eventId)) {
                return;
            }
            JsonNode payload = event.path("payload");
            String applicationGuid = payload.path("applicationGuid").asText(event.path("aggregateId").asText());
            Instant eventAt = Instant.parse(event.path("occurredAt").asText());
            ProjectApplicationProjectionEntity projection = repository.findById(applicationGuid).orElse(null);
            switch (eventType) {
                case "project.application.submitted" -> repository.save(ProjectApplicationProjectionEntity.submitted(
                        applicationGuid, payload.path("projectGuid").asText(), payload.path("applicantGuid").asText(),
                        eventId, eventAt));
                case "project.application.status-changed" -> {
                    if (projection != null) {
                        projection.statusChanged(payload.path("statusCode").asText(), payload.path("approverGuid").asText(null), eventId, eventAt);
                        repository.save(projection);
                    }
                }
                case "project.application.cancelled" -> {
                    if (projection != null) {
                        projection.cancelled(eventId, eventAt);
                        repository.save(projection);
                    }
                }
                default -> { return; }
            }
        } catch (Exception exception) {
            throw new IllegalStateException("Unable to consume project application event", exception);
        } finally {
            if (correlationId != null && !correlationId.isBlank()) {
                MDC.remove(CorrelationIdFilter.MDC_KEY);
            }
        }
    }
}
