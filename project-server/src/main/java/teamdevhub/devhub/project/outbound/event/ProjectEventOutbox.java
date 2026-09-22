package teamdevhub.devhub.project.outbound.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import teamdevhub.devhub.project.outbound.event.persistence.ProjectOutboxEventEntity;
import teamdevhub.devhub.project.outbound.event.persistence.ProjectOutboxEventRepository;
import teamdevhub.devhub.shared.event.IntegrationEvent;
import org.slf4j.MDC;

@Component
@RequiredArgsConstructor
public class ProjectEventOutbox {
    private final ObjectMapper objectMapper;
    private final ProjectOutboxEventRepository repository;

    public void append(IntegrationEvent event) {
        try {
            String correlationId = event.correlationId() == null
                    ? MDC.get("correlationId") : event.correlationId();
            IntegrationEvent enrichedEvent = correlationId == null || correlationId.isBlank()
                    ? event
                    : new IntegrationEvent(event.eventId(), event.eventType(), event.schemaVersion(), event.occurredAt(),
                            event.aggregateType(), event.aggregateId(), correlationId, event.causationId(), event.payload());
            repository.save(ProjectOutboxEventEntity.from(enrichedEvent,
                    objectMapper.writeValueAsString(enrichedEvent.payload())));
        } catch (JsonProcessingException exception) {
            throw new IllegalStateException("Unable to serialize integration event: " + event.eventType(), exception);
        }
    }
}
