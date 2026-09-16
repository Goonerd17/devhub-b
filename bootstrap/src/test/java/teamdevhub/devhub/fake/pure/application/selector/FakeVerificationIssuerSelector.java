package teamdevhub.devhub.fake.pure.application.selector;

import teamdevhub.devhub.platform.core.common.exception.BusinessRuleException;
import teamdevhub.devhub.identity.core.auth.application.selector.verification.VerificationIssuerSelector;
import teamdevhub.devhub.identity.core.auth.port.out.verification.VerificationIssuer;
import teamdevhub.devhub.platform.shared.enums.ErrorCode;
import teamdevhub.devhub.identity.core.auth.application.service.verification.IssuedVerification;
import teamdevhub.devhub.identity.core.auth.domain.vo.verification.VerificationTarget;

import java.util.List;

public class FakeVerificationIssuerSelector implements VerificationIssuerSelector {

    private final List<VerificationIssuer> issuerList;

    public FakeVerificationIssuerSelector(List<VerificationIssuer> issuerList) {
        this.issuerList = issuerList;
    }

    @Override
    public IssuedVerification issueVerification(VerificationTarget verificationTarget) {
        return issuerList.stream()
                .filter(issuer -> issuer.supports(verificationTarget))
                .findFirst()
                .orElseThrow(
                        () -> BusinessRuleException.of(ErrorCode.VERIFICATION_FAIL))
                .issue(verificationTarget);
    }
}
