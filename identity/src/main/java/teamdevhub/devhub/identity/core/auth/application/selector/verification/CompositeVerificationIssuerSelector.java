package teamdevhub.devhub.identity.core.auth.application.selector.verification;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import teamdevhub.devhub.platform.core.common.exception.BusinessRuleException;
import teamdevhub.devhub.identity.core.auth.port.out.verification.VerificationIssuer;
import teamdevhub.devhub.platform.shared.enums.ErrorCode;
import teamdevhub.devhub.identity.core.auth.domain.vo.verification.VerificationTarget;
import teamdevhub.devhub.identity.core.auth.application.service.verification.IssuedVerification;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CompositeVerificationIssuerSelector implements VerificationIssuerSelector {

    private final List<VerificationIssuer> issuerList;

    public IssuedVerification issueVerification(VerificationTarget verificationTarget) {
        return issuerList.stream()
                .filter(issuer -> issuer.supports(verificationTarget))
                .findFirst()
                .orElseThrow(() -> BusinessRuleException.of(ErrorCode.VERIFICATION_FAIL))
                .issue(verificationTarget);
    }
}