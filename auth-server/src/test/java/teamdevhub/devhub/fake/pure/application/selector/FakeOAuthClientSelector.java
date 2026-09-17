package teamdevhub.devhub.fake.pure.application.selector;

import teamdevhub.devhub.auth.core.auth.application.selector.oauth.OAuthClientSelector;
import teamdevhub.devhub.auth.core.auth.domain.VerificationProvider;
import teamdevhub.devhub.auth.core.auth.port.out.oauth.OAuthClient;

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
