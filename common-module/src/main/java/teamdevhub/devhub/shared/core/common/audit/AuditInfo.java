package teamdevhub.devhub.shared.core.common.audit;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record AuditInfo(String registrantGuid, LocalDateTime registeredDate, String modifierGuid, LocalDateTime modifiedDate) {

    private static final AuditInfo EMPTY = new AuditInfo(null, null, null, null);

    public static AuditInfo of(String registrantGuid, LocalDateTime registeredDate, String modifierGuid, LocalDateTime modifiedDate) {
        return new AuditInfo(registrantGuid, registeredDate, modifierGuid, modifiedDate);
    }

    public static AuditInfo empty() {
        return EMPTY;
    }
}
