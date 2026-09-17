package teamdevhub.devhub.auth.outbound.auth.adapter.entity;

import jakarta.persistence.*;
import lombok.*;
import teamdevhub.devhub.shared.outbound.common.persistence.jpa.converter.BooleanToYNConverter;
import teamdevhub.devhub.auth.core.auth.domain.vo.verification.VerificationType;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "verifications")
public class VerificationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "verification_type", nullable = false)
    private VerificationType verificationType;

    @Column(name = "target_value", nullable = false)
    private String targetValue;

    @Column
    private String code;

    @Column
    private LocalDateTime expiredAt;

    @Convert(converter = BooleanToYNConverter.class)
    @Column(nullable = false)
    private boolean verified;
}