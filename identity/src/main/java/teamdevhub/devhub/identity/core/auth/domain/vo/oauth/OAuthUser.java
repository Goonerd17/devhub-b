package teamdevhub.devhub.identity.core.auth.domain.vo.oauth;

import lombok.Builder;
import teamdevhub.devhub.identity.core.auth.domain.VerificationProvider;

@Builder
public record OAuthUser(String oauthId, VerificationProvider verificationProvider, String email) {}
