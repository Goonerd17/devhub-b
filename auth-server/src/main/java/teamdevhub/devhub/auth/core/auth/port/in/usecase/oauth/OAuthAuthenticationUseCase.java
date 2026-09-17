package teamdevhub.devhub.auth.core.auth.port.in.usecase.oauth;

import teamdevhub.devhub.auth.core.auth.application.service.oauth.vo.OAuthAuthorizationResult;
import teamdevhub.devhub.auth.core.auth.domain.VerificationProvider;
import teamdevhub.devhub.auth.core.auth.domain.vo.oauth.OAuthUser;

public interface OAuthAuthenticationUseCase {

    OAuthAuthorizationResult createAuthorizationUrl(String provider);
    OAuthUser handleOAuthCallback(VerificationProvider verificationProvider, String code);
    String issueTempToken(OAuthUser oauthUser);
}
