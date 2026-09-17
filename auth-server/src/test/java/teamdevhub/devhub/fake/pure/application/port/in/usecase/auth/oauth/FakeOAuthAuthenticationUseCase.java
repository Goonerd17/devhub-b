package teamdevhub.devhub.fake.pure.application.port.in.usecase.auth.oauth;

import lombok.Setter;
import teamdevhub.devhub.auth.core.auth.application.service.oauth.vo.OAuthAuthorizationResult;
import teamdevhub.devhub.auth.core.auth.domain.VerificationProvider;
import teamdevhub.devhub.auth.core.auth.domain.vo.oauth.OAuthUser;
import teamdevhub.devhub.auth.core.auth.port.in.usecase.oauth.OAuthAuthenticationUseCase;

import java.util.Objects;

import static teamdevhub.devhub.constant.UserTestConstant.*;

@Setter
public class FakeOAuthAuthenticationUseCase implements OAuthAuthenticationUseCase {

    private OAuthUser oauthUser;
    private String tempToken;

    @Override
    public OAuthAuthorizationResult createAuthorizationUrl(String provider) {
        return OAuthAuthorizationResult.of("https://oauth.test/" + provider, "test-state");
    }

    @Override
    public OAuthUser handleOAuthCallback(VerificationProvider verificationProvider, String code) {
        return Objects.requireNonNullElseGet(oauthUser, () -> new OAuthUser(TEST_OAUTH_ID_1, verificationProvider, TEST_EMAIL_1));
    }

    @Override
    public String issueTempToken(OAuthUser oauthUser) {
        return Objects.requireNonNullElse(tempToken, TEMP_TOKEN);
    }
}
