package teamdevhub.devhub.auth.outbound.auth.infrastructure.oauth;

import lombok.Builder;
import teamdevhub.devhub.auth.core.auth.domain.VerificationProvider;

@Builder
public record OAuthUser(String oauthId, VerificationProvider verificationProvider, String email) {}
