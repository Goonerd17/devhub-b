package teamdevhub.devhub.fake.pure.application.selector;

import teamdevhub.devhub.shared.core.common.exception.BusinessRuleException;
import teamdevhub.devhub.auth.core.auth.application.selector.verification.VerificationIssuerSelector;
import teamdevhub.devhub.auth.core.auth.port.out.verification.VerificationIssuer;
import teamdevhub.devhub.shared.shared.enums.ErrorCode;
import teamdevhub.devhub.auth.core.auth.application.service.verification.IssuedVerification;
import teamdevhub.devhub.auth.core.auth.domain.vo.verification.VerificationTarget;

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
