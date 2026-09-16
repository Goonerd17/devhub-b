package teamdevhub.devhub.identity.core.auth.domain;

import lombok.Builder;
import lombok.Getter;
import teamdevhub.devhub.identity.core.auth.domain.vo.verification.VerificationMessage;
import teamdevhub.devhub.identity.core.auth.domain.vo.verification.VerificationTarget;
import teamdevhub.devhub.platform.shared.enums.ErrorCode;
import teamdevhub.devhub.platform.core.common.exception.DomainRuleException;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
public class Verification {

    private final Long id;
    private final VerificationTarget verificationTarget;
    private final String code;
    private final LocalDateTime expiredAt;
    private boolean verified;

    @Builder
    private Verification(
            Long id,
            VerificationTarget verificationTarget,
            String code,
            LocalDateTime expiredAt,
            boolean verified
    ) {
        this.id = id;
        this.verificationTarget = Objects.requireNonNull(verificationTarget);
        this.code = code;
        this.expiredAt = expiredAt;
        this.verified = verified;
    }

    public static Verification issue(VerificationTarget verificationTarget, VerificationMessage verificationMessage) {
        return Verification.builder()
                .id(null)
                .verificationTarget(verificationTarget)
                .code(verificationMessage.code())
                .expiredAt(verificationMessage.expiredAt())
                .verified(false)
                .build();
    }

    public static Verification of(
            Long id,
            VerificationTarget verificationTarget,
            String code,
            LocalDateTime expiredAt,
            boolean verified
    ) {
        return Verification.builder()
                .id(id)
                .verificationTarget(verificationTarget)
                .code(code)
                .expiredAt(expiredAt)
                .verified(verified)
                .build();
    }

    public void confirm(String code, LocalDateTime now) {
        boolean success = verify(code, now);
        if (!success) {
            throw DomainRuleException.of(ErrorCode.VERIFICATION_FAIL);
        }
    }

    public void assertValid(LocalDateTime now) {
        if (!isVerified() || isExpired(now)) {
            throw DomainRuleException.of(ErrorCode.VERIFICATION_FAIL);
        }
    }

    private boolean verify(String inputCode, LocalDateTime now) {
        if (verified) return true;
        if (expiredAt.isBefore(now)) return false;
        if (!Objects.equals(code, inputCode)) return false;
        this.verified = true;
        return true;
    }

    private boolean isExpired(LocalDateTime now) {
        return expiredAt.isBefore(now);
    }
}
