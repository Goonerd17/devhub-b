package teamdevhub.devhub.auth.outbound.auth.adapter.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import teamdevhub.devhub.member.api.MemberRole;
import teamdevhub.devhub.auth.core.auth.domain.VerificationProvider;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
@Table(
        name = "user_oauth_credentials",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_oauth_provider_id",
                        columnNames = {"provider", "oauthId"}
                )
        }
)
public class OAuthCredentialEntity {

    @Id
    @Column(length = 32, nullable = false, unique = true)
    private String userGuid;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VerificationProvider provider;

    @Column(nullable = false)
    private String oauthId;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private MemberRole userRole;
}
