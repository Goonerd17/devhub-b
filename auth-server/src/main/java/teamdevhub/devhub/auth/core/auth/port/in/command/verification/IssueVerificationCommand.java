package teamdevhub.devhub.auth.core.auth.port.in.command.verification;

import lombok.Builder;
import teamdevhub.devhub.auth.core.auth.domain.vo.verification.VerificationTarget;

@Builder
public record IssueVerificationCommand(VerificationTarget verificationTarget) { }
