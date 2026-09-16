package teamdevhub.devhub.identity.outbound.auth.adapter.mapper;

import teamdevhub.devhub.identity.outbound.auth.adapter.entity.VerificationEntity;
import teamdevhub.devhub.identity.core.auth.domain.Verification;
import teamdevhub.devhub.identity.core.auth.domain.vo.verification.VerificationTarget;

public class VerificationMapper {

    public static VerificationEntity toEntity(Verification verification) {
        return VerificationEntity.builder()
                .id(verification.getId())
                .verificationType(verification.getVerificationTarget().verificationType())
                .targetValue(verification.getVerificationTarget().value())
                .code(verification.getCode())
                .expiredAt(verification.getExpiredAt())
                .verified(verification.isVerified())
                .build();
    }

    public static Verification toDomain(VerificationEntity verificationEntity) {
        return Verification.of(
                verificationEntity.getId(),
                VerificationTarget.fromEntity(verificationEntity.getVerificationType(), verificationEntity.getTargetValue()),
                verificationEntity.getCode(),
                verificationEntity.getExpiredAt(),
                verificationEntity.isVerified()
        );
    }
}
