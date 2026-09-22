package teamdevhub.devhub.notification.outbound.event;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import teamdevhub.devhub.notification.core.domain.NotificationType;
import teamdevhub.devhub.notification.core.port.in.NotificationUseCase;
import teamdevhub.devhub.notification.core.port.in.command.CreateNotificationCommand;

import java.util.List;
import java.time.Instant;

@Component
@RequiredArgsConstructor
public class ProjectEventNotificationConsumer {
    private final ObjectMapper objectMapper;
    private final NotificationUseCase notificationUseCase;
    private final ProcessedNotificationEventRepository processedEventRepository;

    @KafkaListener(topics = "${events.kafka.topic:devhub.project.events}",
            groupId = "${spring.kafka.consumer.group-id:notification-project-events}")
    @Transactional
    public void consume(String message) {
        try {
            JsonNode event = objectMapper.readTree(message);
            if (event.path("schemaVersion").asInt(0) != 1) {
                throw new IllegalArgumentException("Unsupported project event schema version");
            }
            String eventType = event.path("eventType").asText();
            if (!"project.closed".equals(eventType) && !"project.application.status-changed".equals(eventType)) {
                return;
            }
            String eventId = event.path("eventId").asText();
            if (eventId.isBlank() || processedEventRepository.existsById(eventId)) {
                return;
            }
            JsonNode payload = event.path("payload");
            if ("project.closed".equals(eventType)) {
                String ownerGuid = payload.path("ownerGuid").asText();
                String projectGuid = payload.path("projectGuid").asText(event.path("aggregateId").asText());
                String title = payload.path("title").asText("");
                if (ownerGuid.isBlank() || projectGuid.isBlank()) {
                    throw new IllegalArgumentException("project.closed event requires ownerGuid and projectGuid");
                }
                notificationUseCase.createNotification(CreateNotificationCommand.builder()
                        .receiverId(ownerGuid)
                        .type(NotificationType.PROJECT_FINISHED)
                        .messageArgs(List.of(title))
                        .redirectTarget(projectGuid)
                        .build());
            } else {
                String applicantGuid = payload.path("applicantGuid").asText();
                String projectGuid = payload.path("projectGuid").asText();
                String statusCode = payload.path("statusCode").asText();
                if (applicantGuid.isBlank() || projectGuid.isBlank()) {
                    throw new IllegalArgumentException("application status event requires applicantGuid and projectGuid");
                }
                NotificationType type = "3302".equals(statusCode)
                        ? NotificationType.PROJECT_APPROVED : NotificationType.PROJECT_REJECTED;
                notificationUseCase.createNotification(CreateNotificationCommand.builder()
                        .receiverId(applicantGuid)
                        .type(type)
                        .messageArgs(List.of("프로젝트"))
                        .redirectTarget(projectGuid)
                        .build());
            }
            processedEventRepository.save(new ProcessedNotificationEventEntity(eventId, Instant.now()));
        } catch (Exception exception) {
            throw new IllegalStateException("Unable to consume project notification event", exception);
        }
    }
}
