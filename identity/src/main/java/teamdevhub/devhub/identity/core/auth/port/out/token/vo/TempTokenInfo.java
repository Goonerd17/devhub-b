package teamdevhub.devhub.identity.core.auth.port.out.token.vo;

import lombok.Builder;
import teamdevhub.devhub.identity.core.auth.domain.VerificationProvider;

@Builder
public record TempTokenInfo(String oauthId, VerificationProvider verificationProvider, String email) {}
