package teamdevhub.devhub.member.outbound.user.adapter.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        name = "user_position",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {"user_guid", "position_cd"}
                )
        }
)
public class UserPositionEntity {

    @Id
    @Column(name = "user__position_guid")
    private String userPositionGuid;

    @Column(name = "user_guid", nullable = false)
    private String userGuid;

    @Column(name = "position_cd", nullable = false, length = 10)
    private String positionCd;
}
