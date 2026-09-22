package teamdevhub.devhub.shared.outbound.common.persistence.jpa.audit;

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    @CreatedBy
    @Column(updatable = false)
    private String registrantGuid;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime registeredDate;

    @LastModifiedBy
    @Column
    private String modifierGuid;

    @LastModifiedDate
    @Column
    private LocalDateTime modifiedDate;
}