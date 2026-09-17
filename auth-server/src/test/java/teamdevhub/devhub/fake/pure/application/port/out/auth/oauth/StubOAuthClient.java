package teamdevhub.devhub.fake.pure.application.port.out.auth.oauth;

import teamdevhub.devhub.auth.core.auth.domain.VerificationProvider;
import teamdevhub.devhub.auth.core.auth.domain.vo.oauth.OAuthUser;
import teamdevhub.devhub.auth.core.auth.port.out.oauth.OAuthClient;

public class StubOAuthClient implements OAuthClient {

    private final VerificationProvider supportedProvider;

    public StubOAuthClient(VerificationProvider supportedProvider) {
        this.supportedProvider = supportedProvider;
    }

    @Override
    public boolean supports(VerificationProvider verificationProvider) {
        return supportedProvider == verificationProvider;
    }

    @Override
    public String getAuthorizationUrl(String state) {
        return "https://fake-oauth.com/auth";
    }

    @Override
    public OAuthUser fetchUser(String authorizationCode) {
        throw new UnsupportedOperationException("selector 테스트 범위 아님");
    }
}
