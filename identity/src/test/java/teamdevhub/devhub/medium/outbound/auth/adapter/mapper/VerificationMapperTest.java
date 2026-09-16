package teamdevhub.devhub.identity.outbound.auth.adapter.mapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.identity.outbound.auth.adapter.entity.VerificationEntity;
import teamdevhub.devhub.identity.outbound.auth.adapter.mapper.VerificationMapper;
import teamdevhub.devhub.identity.core.auth.domain.Verification;
import teamdevhub.devhub.identity.core.auth.domain.vo.verification.VerificationMessage;
import teamdevhub.devhub.identity.core.auth.domain.vo.verification.VerificationTarget;
import teamdevhub.devhub.identity.core.auth.domain.vo.verification.VerificationType;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class VerificationMapperTest {

    private static final String TEST_EMAIL_1 = "user1@example.com";
    private static final String TEST_EMAIL_CODE = "123456";

    @Test
    @DisplayName("Verification_??VerificationEntity_濡??щ컮瑜닿쾶_蹂?섑븳??")
    void convertsDomainToEntity() {
        // given
        VerificationTarget verificationTarget = new VerificationTarget(VerificationType.EMAIL, TEST_EMAIL_1);
        VerificationMessage verificationMessage = new VerificationMessage(TEST_EMAIL_CODE, LocalDateTime.now().plusHours(1));
        Verification verification = Verification.issue(verificationTarget,verificationMessage);

        // when
        VerificationEntity verificationEntity = VerificationMapper.toEntity(verification);

        // then
        assertThat(verificationEntity.getVerificationType()).isEqualTo(verification.getVerificationTarget().verificationType());
        assertThat(verificationEntity.getTargetValue()).isEqualTo(verification.getVerificationTarget().value());
        assertThat(verificationEntity.getCode()).isEqualTo(verification.getCode());
        assertThat(verificationEntity.getExpiredAt()).isEqualTo(verification.getExpiredAt());
        assertThat(verificationEntity.isVerified()).isEqualTo(verification.isVerified());
    }

    @Test
    @DisplayName("VerificationEntity_瑜??꾨찓??Verification_?쇰줈_?щ컮瑜닿쾶_蹂?섑븳??")
    void convertsEntityToDomain() {
        // given
        VerificationEntity verificationEntity = VerificationEntity.builder()
                .id(1L)
                .verificationType(VerificationType.EMAIL)
                .targetValue(TEST_EMAIL_1)
                .code(TEST_EMAIL_CODE)
                .expiredAt(LocalDateTime.now().plusHours(1))
                .verified(false)
                .build();

        // when
        Verification verification = VerificationMapper.toDomain(verificationEntity);

        // then
        assertThat(verification.getId()).isEqualTo(verificationEntity.getId());
        assertThat(verification.getVerificationTarget().verificationType()).isEqualTo(verificationEntity.getVerificationType());
        assertThat(verification.getVerificationTarget().value()).isEqualTo(verificationEntity.getTargetValue());
        assertThat(verification.getCode()).isEqualTo(verificationEntity.getCode());
        assertThat(verification.getExpiredAt()).isEqualTo(verificationEntity.getExpiredAt());
        assertThat(verification.isVerified()).isEqualTo(verificationEntity.isVerified());
    }
}
