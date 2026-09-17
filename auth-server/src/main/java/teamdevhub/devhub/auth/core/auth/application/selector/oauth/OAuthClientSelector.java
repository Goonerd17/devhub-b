package teamdevhub.devhub.auth.core.auth.application.selector.oauth;

import teamdevhub.devhub.auth.core.auth.domain.VerificationProvider;
import teamdevhub.devhub.auth.core.auth.port.out.oauth.OAuthClient;

public interface OAuthClientSelector {

     OAuthClient select(VerificationProvider verificationProvider);
}
