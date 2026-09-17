package teamdevhub.devhub.auth.core.auth.port.out.token.vo;

import lombok.Builder;
import teamdevhub.devhub.auth.core.auth.domain.VerificationProvider;

@Builder
public record TempTokenInfo(String oauthId, VerificationProvider verificationProvider, String email) {}
