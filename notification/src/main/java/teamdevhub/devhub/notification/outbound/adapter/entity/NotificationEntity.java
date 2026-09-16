package teamdevhub.devhub.notification.outbound.adapter.entity;

import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import teamdevhub.devhub.platform.outbound.common.persistence.jpa.converter.BooleanToYNConverter;
import teamdevhub.devhub.platform.outbound.common.persistence.jpa.audit.BaseEntity;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
@Table(
        name = "notification",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_notification_guid",
                        columnNames = "notificationGuid"
                )
        }
)
public class NotificationEntity extends BaseEntity {
	
	@Id
	@Column(length = 32, nullable = false, unique = true)
    private String notificationGuid;

    @Column(length = 32, nullable = false)
    private String receiver;
    
    @Column(name="type_cd", length = 10, nullable = false)
    private String typeCd;
    
    @Column(name = "content", nullable = false)
    private String content;
    
    @Convert(converter = BooleanToYNConverter.class)
    @Column(nullable = false)
    private boolean checked;
}
