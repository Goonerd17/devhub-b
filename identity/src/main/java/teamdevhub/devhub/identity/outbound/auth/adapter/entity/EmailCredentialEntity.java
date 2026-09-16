package teamdevhub.devhub.identity.outbound.auth.adapter.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import teamdevhub.devhub.member.api.MemberRole;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
@Table(
        name = "user_email_credentials",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_email_credentials_email",
                        columnNames = "email"
                )
        }
)
public class EmailCredentialEntity {

    @Id
    @Column(length = 32, nullable = false, unique = true)
    private String userGuid;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private MemberRole userRole;
}
