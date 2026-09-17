package teamdevhub.devhub.auth.core.auth.domain.vo.oauth;

import lombok.Builder;
import teamdevhub.devhub.auth.core.auth.domain.VerificationProvider;

@Builder
public record OAuthUser(String oauthId, VerificationProvider verificationProvider, String email) {}
