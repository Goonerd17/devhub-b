package teamdevhub.devhub.identity.outbound.auth.infrastructure.oauth;

import lombok.Builder;
import teamdevhub.devhub.identity.core.auth.domain.VerificationProvider;

@Builder
public record OAuthUser(String oauthId, VerificationProvider verificationProvider, String email) {}
