package teamdevhub.devhub.member.outbound.user.adapter.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import teamdevhub.devhub.member.core.user.domain.vo.UserRole;
import teamdevhub.devhub.platform.outbound.common.persistence.jpa.audit.BaseEntity;
import teamdevhub.devhub.platform.outbound.common.persistence.jpa.converter.BooleanToYNConverter;

import java.time.LocalDateTime;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
@Table(
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_users_email",
                        columnNames = "userGuid"
                )
        }
)
public class UserEntity extends BaseEntity {

    @Id
    @Column(length = 32, nullable = false, unique = true)
    private String userGuid;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private UserRole userRole;

    @Column
    private String username;

    @Column(length = 500)
    private String introduction;

    @Column
    private String fileGuid;

    @Column(nullable = false)
    private double mannerDegree;

    @Convert(converter = BooleanToYNConverter.class)
    @Column(nullable = false)
    private boolean blocked;

    @Column
    private LocalDateTime blockEndDate;

    @Convert(converter = BooleanToYNConverter.class)
    @Column(nullable = false)
    private boolean deleted;

    @Column
    private LocalDateTime lastLoginDateTime;
}
