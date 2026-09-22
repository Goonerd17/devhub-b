package teamdevhub.devhub.query.outbound.project;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.query.outbound.project.persistence.ProjectProjectionEntity;
import teamdevhub.devhub.query.outbound.project.persistence.ProjectProjectionRepository;

import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

class ProjectEventKafkaConsumerTest {
    private ProjectProjectionRepository repository;
    private ProjectEventKafkaConsumer consumer;

    @BeforeEach
    void setUp() {
        repository = mock(ProjectProjectionRepository.class);
        consumer = new ProjectEventKafkaConsumer(new ObjectMapper(), repository);
    }

    @Test
    void createdEventCreatesProjection() {
        when(repository.existsByLastEventId("evt-1")).thenReturn(false);

        consumer.consume(event("evt-1", "project.created", "project-1",
                "{\"ownerGuid\":\"member-1\",\"title\":\"Kafka project\",\"category\":\"WEB\",\"username\":\"dev\",\"recruitmentStartDate\":\"2026-01-01\",\"recruitmentEndDate\":\"2026-12-31\",\"capacityClosed\":false,\"recruitStatus\":\"3201\"}"));

        verify(repository).save(any(ProjectProjectionEntity.class));
    }

    @Test
    void duplicateEventIsIgnored() {
        when(repository.existsByLastEventId("evt-1")).thenReturn(true);

        consumer.consume(event("evt-1", "project.created", "project-1", "{}"));

        verify(repository, org.mockito.Mockito.never()).save(any(ProjectProjectionEntity.class));
    }

    @Test
    void updateAndCloseEventsChangeExistingProjection() {
        when(repository.existsByLastEventId(any())).thenReturn(false);
        ProjectProjectionEntity projection = ProjectProjectionEntity.created(
                "project-1", "member-1", "before", "WEB", "dev", null,
                null, null, null, false, "3201", "evt-0", Instant.now());
        when(repository.findById("project-1")).thenReturn(Optional.of(projection));

        consumer.consume(event("evt-2", "project.updated", "project-1",
                "{\"title\":\"after\",\"category\":\"APP\",\"username\":\"owner\",\"capacityClosed\":false,\"recruitStatus\":\"3201\"}"));
        assertThat(projection.getTitle()).isEqualTo("after");
        assertThat(projection.getLifecycleStatus()).isEqualTo("UPDATED");

        consumer.consume(event("evt-3", "project.closed", "project-1", "{}"));
        assertThat(projection.getLifecycleStatus()).isEqualTo("CLOSED");
    }

    @Test
    void deletedEventRemovesExistingProjection() {
        when(repository.existsByLastEventId("evt-4")).thenReturn(false);
        ProjectProjectionEntity projection = ProjectProjectionEntity.created(
                "project-1", "member-1", "before", "WEB", "dev", null,
                null, null, null, false, "3201", "evt-0", Instant.now());
        when(repository.findById("project-1")).thenReturn(Optional.of(projection));

        consumer.consume(event("evt-4", "project.deleted", "project-1", "{}"));

        verify(repository).delete(projection);
    }

    private static String event(String eventId, String eventType, String aggregateId, String payload) {
        return "{\"schemaVersion\":1,\"eventId\":\"" + eventId + "\",\"eventType\":\"" + eventType
                + "\",\"occurredAt\":\"2026-09-22T00:00:00Z\",\"aggregateId\":\""
                + aggregateId + "\",\"payload\":" + payload + "}";
    }
}
