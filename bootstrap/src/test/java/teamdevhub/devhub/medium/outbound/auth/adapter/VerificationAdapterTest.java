package teamdevhub.devhub.medium.outbound.auth.adapter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import teamdevhub.devhub.platform.outbound.common.exception.AdapterDataException;
import teamdevhub.devhub.identity.outbound.auth.persistence.JpaVerificationRepository;
import teamdevhub.devhub.identity.outbound.auth.adapter.VerificationAdapter;
import teamdevhub.devhub.identity.outbound.auth.adapter.mapper.VerificationMapper;
import teamdevhub.devhub.platform.shared.enums.ErrorCode;
import teamdevhub.devhub.identity.core.auth.domain.Verification;
import teamdevhub.devhub.identity.core.auth.domain.vo.verification.VerificationMessage;
import teamdevhub.devhub.identity.core.auth.domain.vo.verification.VerificationTarget;
import teamdevhub.devhub.identity.core.auth.domain.vo.verification.VerificationType;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(classes = teamdevhub.devhub.DevhubApplication.class)
@Transactional
class VerificationAdapterTest {

    @Autowired
    private VerificationAdapter verificationAdapter;

    @Autowired
    private JpaVerificationRepository jpaVerificationRepository;

    @BeforeEach
    void init() {
        jpaVerificationRepository.deleteAll();
    }

    @Test
    @DisplayName("?몄쬆?뺣낫瑜???ν븯硫?DB????λ맂??")
    void saveVerification() {
        // given
        VerificationTarget verificationTarget = new VerificationTarget(VerificationType.EMAIL, "test@email.com");
        VerificationMessage verificationMessage = new VerificationMessage("123456",LocalDateTime.now().plusMinutes(5));
        Verification verification = Verification.issue(verificationTarget, verificationMessage);

        // when
        verificationAdapter.save(verification);

        // then
        assertThat(
                jpaVerificationRepository
                        .findTopByVerificationTypeAndTargetValueOrderByExpiredAtDesc(
                                VerificationType.EMAIL,
                                "test@email.com"
                        )
        ).isPresent();
    }

    @Test
    @DisplayName("?몄쬆??곸쑝濡??몄쬆?뺣낫瑜?議고쉶?쒕떎")
    void findByVerificationTarget() {
        // given
        VerificationTarget verificationTarget = new VerificationTarget(VerificationType.EMAIL, "test@email.com");
        VerificationMessage verificationMessage = new VerificationMessage("654321",LocalDateTime.now().plusMinutes(5));
        Verification verification = Verification.issue(verificationTarget, verificationMessage);

        jpaVerificationRepository.save(VerificationMapper.toEntity(verification));

        // when
        Verification found = verificationAdapter.findByVerificationTarget(verificationTarget);

        // then
        assertThat(found).isNotNull();
        assertThat(found.getVerificationTarget()).isEqualTo(verificationTarget);
        assertThat(found.getCode()).isEqualTo("654321");
    }

    @Test
    @DisplayName("?몄쬆?뺣낫媛_?놁쑝硫??덉쇅瑜?諛쒖깮?쒗궓??")
    void findByVerificationTarget_notExists_throwsException() {
        // given
        VerificationTarget verificationTarget = new VerificationTarget(VerificationType.EMAIL, "notfound@email.com");

        // when then
        assertThatThrownBy(
                () -> verificationAdapter.findByVerificationTarget(verificationTarget))
                .isInstanceOf(AdapterDataException.class)
                .hasMessageContaining(ErrorCode.VERIFICATION_NOT_EXISTED.getMessage());
    }

    @Test
    @DisplayName("?몄쬆??곸쑝濡??몄쬆?뺣낫瑜???젣?쒕떎")
    void deleteByVerificationTarget() {
        // given
        VerificationTarget verificationTarget = new VerificationTarget(VerificationType.EMAIL, "deleteByFileGuid@email.com");
        VerificationMessage verificationMessage = new VerificationMessage("000000",LocalDateTime.now().plusMinutes(5));
        Verification verification = Verification.issue(verificationTarget, verificationMessage);

        jpaVerificationRepository.save(VerificationMapper.toEntity(verification));

        // when
        verificationAdapter.deleteByVerificationTarget(verificationTarget);

        // then
        assertThat(jpaVerificationRepository.findTopByVerificationTypeAndTargetValueOrderByExpiredAtDesc(VerificationType.EMAIL, "deleteByFileGuid@email.com")).isEmpty();
    }
}
