package teamdevhub.devhub.identity.core.auth.application.selector.oauth;

import teamdevhub.devhub.identity.core.auth.domain.VerificationProvider;
import teamdevhub.devhub.identity.core.auth.port.out.oauth.OAuthClient;

public interface OAuthClientSelector {

     OAuthClient select(VerificationProvider verificationProvider);
}
