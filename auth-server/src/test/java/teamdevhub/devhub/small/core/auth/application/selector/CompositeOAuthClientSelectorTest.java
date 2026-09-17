package teamdevhub.devhub.auth.core.auth.application.selector;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import teamdevhub.devhub.auth.core.auth.application.selector.oauth.CompositeOAuthClientSelector;
import teamdevhub.devhub.shared.core.common.exception.BusinessRuleException;
import teamdevhub.devhub.shared.shared.enums.ErrorCode;
import teamdevhub.devhub.auth.core.auth.domain.VerificationProvider;
import teamdevhub.devhub.fake.pure.application.port.out.auth.oauth.StubOAuthClient;
import teamdevhub.devhub.auth.core.auth.port.out.oauth.OAuthClient;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class CompositeOAuthClientSelectorTest {

    private CompositeOAuthClientSelector compositeOAuthClientSelector;

    @BeforeEach
    void init() {
        OAuthClient githubOAuthClient = new StubOAuthClient(VerificationProvider.GITHUB);
        OAuthClient googleOAuthClient = new StubOAuthClient(VerificationProvider.GOOGLE);

        compositeOAuthClientSelector = new CompositeOAuthClientSelector(List.of(githubOAuthClient, googleOAuthClient));
    }

    @Test
    @DisplayName("GOOGLE_OAUTH_이면_GOOGLE_OAuthClient_가_선택된다")
    void select_githubProvider_returnsGithubClient() {
        // given, when
        OAuthClient selectedOauClient = compositeOAuthClientSelector.select(VerificationProvider.GOOGLE);

        // then
        assertThat(selectedOauClient.supports(VerificationProvider.GOOGLE)).isTrue();
    }

    @Test
    @DisplayName("지원하지_않는_Provider_이면_BusinessRuleException_이_발생한다")
    void select_unsupportedProvider_throwsException() {
        // given, when, then
        assertThatThrownBy(
                () -> compositeOAuthClientSelector.select(VerificationProvider.KAKAO))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining(ErrorCode.OAUTH_FAIL.getMessage());
    }
}