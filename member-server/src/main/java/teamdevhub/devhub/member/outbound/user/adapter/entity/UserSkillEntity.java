package teamdevhub.devhub.member.outbound.user.adapter.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        name = "user_skill",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {"user_guid", "skill_cd"}
                )
        }
)
public class UserSkillEntity {

    @Id
    @Column(name = "user_skill_guid")
    private String userSkillGuid;

    @Column(name = "user_guid", nullable = false)
    private String userGuid;

    @Column(name = "skill_cd", nullable = false, length = 10)
    private String skillCd;
}
