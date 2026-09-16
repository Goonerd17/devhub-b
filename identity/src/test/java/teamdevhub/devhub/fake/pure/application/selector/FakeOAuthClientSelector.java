package teamdevhub.devhub.fake.pure.application.selector;

import teamdevhub.devhub.identity.core.auth.application.selector.oauth.OAuthClientSelector;
import teamdevhub.devhub.identity.core.auth.domain.VerificationProvider;
import teamdevhub.devhub.identity.core.auth.port.out.oauth.OAuthClient;

public class FakeOAuthClientSelector implements OAuthClientSelector {

    private final OAuthClient oauthClient;

    public FakeOAuthClientSelector(OAuthClient oauthClient) {
        this.oauthClient = oauthClient;
    }

    @Override
    public OAuthClient select(VerificationProvider provider) {
        return oauthClient;
    }
}
