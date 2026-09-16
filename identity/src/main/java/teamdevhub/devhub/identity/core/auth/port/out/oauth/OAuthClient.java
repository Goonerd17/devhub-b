package teamdevhub.devhub.identity.core.auth.port.out.oauth;

import teamdevhub.devhub.identity.core.auth.domain.VerificationProvider;
import teamdevhub.devhub.identity.core.auth.domain.vo.oauth.OAuthUser;

public interface OAuthClient {

    boolean supports(VerificationProvider verificationProvider);
    String getAuthorizationUrl(String state);
    OAuthUser fetchUser(String authorizationCode);
}