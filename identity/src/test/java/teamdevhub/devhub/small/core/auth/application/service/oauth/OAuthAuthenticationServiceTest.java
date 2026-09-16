package teamdevhub.devhub.identity.core.auth.application.service.oauth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.identity.core.auth.application.service.oauth.OAuthAuthenticationService;
import teamdevhub.devhub.identity.core.auth.application.service.oauth.vo.OAuthAuthorizationResult;
import teamdevhub.devhub.fake.pure.application.port.out.auth.oauth.FakeOAuthClient;
import teamdevhub.devhub.fake.pure.application.provider.FakeTokenIssueProvider;
import teamdevhub.devhub.fake.pure.application.provider.FakeUuidIdentifierProvider;
import teamdevhub.devhub.fake.pure.application.selector.FakeOAuthClientSelector;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static teamdevhub.devhub.constant.UserTestConstant.TEST_USER_GUID_1;

public class OAuthAuthenticationServiceTest {

    private OAuthAuthenticationService oauthAuthenticationService;

    private FakeOAuthClient oauthClient;

    @BeforeEach
    void init() {
        oauthClient = new FakeOAuthClient();
        FakeOAuthClientSelector fakeOAuthClientSelector = new FakeOAuthClientSelector(oauthClient);
        FakeTokenIssueProvider tokenIssueProvider = new FakeTokenIssueProvider();
        FakeUuidIdentifierProvider identifierProvider = new FakeUuidIdentifierProvider(TEST_USER_GUID_1);

        oauthAuthenticationService = new OAuthAuthenticationService(tokenIssueProvider, fakeOAuthClientSelector, identifierProvider);
    }

    @Test
    @DisplayName("OAuth_인증_제공자에_따른_리다이렉트_URL_을_생성할_수_있다")
    void handleOAuthCallbackCreateAuthorizationUrlWithProvider() {
        // given
        String provider = "google";

        // when
        OAuthAuthorizationResult result = oauthAuthenticationService.createAuthorizationUrl(provider);

        // then
        assertThat(result.url()).isEqualTo("https://oauth.test/authorize/");
        assertThat(result.state()).isEqualTo(TEST_USER_GUID_1);
    }
}
