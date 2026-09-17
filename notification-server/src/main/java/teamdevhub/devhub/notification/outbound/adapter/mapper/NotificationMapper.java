package teamdevhub.devhub.notification.outbound.adapter.mapper;

import teamdevhub.devhub.shared.core.common.audit.AuditInfo;
import teamdevhub.devhub.notification.core.domain.Notification;
import teamdevhub.devhub.notification.outbound.adapter.entity.NotificationEntity;

public class NotificationMapper {

    public static Notification toDomain(NotificationEntity notificationEntity) {
        return Notification.builder()
                .notificationGuid(notificationEntity.getNotificationGuid())
                .content(notificationEntity.getContent())
                .isChecked(notificationEntity.isChecked())
                .typeCd(notificationEntity.getTypeCd())
                .receiver(notificationEntity.getReceiver())
                .auditInfo(toAuditInfo(notificationEntity))
                .build();
    }

    private static AuditInfo toAuditInfo(NotificationEntity notificationEntity) {
        return AuditInfo.of(
                notificationEntity.getRegistrantGuid(),
                notificationEntity.getRegisteredDate(),
                notificationEntity.getModifierGuid(),
                notificationEntity.getModifiedDate()
        );
    }

    public static NotificationEntity toEntity(Notification notification) {
        return NotificationEntity.builder()
                .notificationGuid(notification.getNotificationGuid())
                .content(notification.getContent())
                .checked(notification.isChecked())
                .typeCd(notification.getTypeCd())
                .receiver(notification.getReceiver())
                .build();
    }
}
