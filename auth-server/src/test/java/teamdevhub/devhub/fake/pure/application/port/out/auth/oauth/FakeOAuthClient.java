package teamdevhub.devhub.fake.pure.application.port.out.auth.oauth;

import teamdevhub.devhub.auth.core.auth.domain.VerificationProvider;
import teamdevhub.devhub.auth.core.auth.domain.vo.oauth.OAuthUser;
import teamdevhub.devhub.auth.core.auth.port.out.oauth.OAuthClient;

public class FakeOAuthClient implements OAuthClient {

    private OAuthUser oauthUser;

    public void withOAuthUser(OAuthUser oauthUser) {
        this.oauthUser = oauthUser;
    }

    @Override
    public boolean supports(VerificationProvider verificationProvider) {
        return true;
    }

    @Override
    public String getAuthorizationUrl(String state) {
        return "https://oauth.test/authorize/";
    }

    @Override
    public OAuthUser fetchUser(String authorizationCode) {
        return oauthUser;
    }
}

