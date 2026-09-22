package teamdevhub.devhub.shared.event;

import java.time.Instant;

/**
 * 서비스 간 이벤트의 공통 메타데이터 계약입니다.
 * 도메인 Entity 자체를 공유하지 않고, 버전이 있는 불변 이벤트만 전달합니다.
 */
public record IntegrationEvent(
        String eventId,
        String eventType,
        int schemaVersion,
        Instant occurredAt,
        String aggregateType,
        String aggregateId,
        String correlationId,
        String causationId,
        Object payload
) {
    public IntegrationEvent {
        if (schemaVersion < 1) {
            throw new IllegalArgumentException("schemaVersion must be positive");
        }
    }
}
