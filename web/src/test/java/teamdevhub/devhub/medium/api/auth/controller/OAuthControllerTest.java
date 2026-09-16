package teamdevhub.devhub.web.api.auth.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import teamdevhub.devhub.web.api.auth.controller.OAuthController;
import teamdevhub.devhub.web.api.auth.model.response.TokenResponseDto;
import teamdevhub.devhub.web.api.user.model.SignupOAuthRequestDto;
import teamdevhub.devhub.web.api.web.model.response.DataApiResponseDto;
import teamdevhub.devhub.identity.core.auth.application.service.oauth.vo.OAuthResult;
import teamdevhub.devhub.identity.core.auth.application.service.oauth.vo.OAuthAuthorizationResult;
import teamdevhub.devhub.identity.core.auth.port.in.facade.OAuthFacade;
import teamdevhub.devhub.platform.core.common.exception.BusinessRuleException;
import teamdevhub.devhub.web.api.auth.controller.CookieFactory;
import teamdevhub.devhub.web.core.user.port.in.facade.UserSignupFacade;
import teamdevhub.devhub.platform.shared.enums.ErrorCode;
import teamdevhub.devhub.web.shared.enums.SuccessCode;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class OAuthControllerTest {

    private OAuthController oauthController;

    private UserSignupFacade userSignupFacade;
    private OAuthFacade oauthFacade;

    @BeforeEach
    void init() {
        userSignupFacade = Mockito.mock(UserSignupFacade.class);
        oauthFacade = Mockito.mock(OAuthFacade.class);

        oauthController = new OAuthController(userSignupFacade, oauthFacade, new CookieFactory());
    }

    @Test
    @DisplayName("OAuth_로그인_요청시_Provider_인증_URL_로_리다이렉트되고_state_쿠키가_설정된다")
    void redirectToProvider_redirectsToauthorizationUrl() throws Exception {
        // given
        String provider = "google";
        String authorizationUrl = "https://google.com/oauth/authorize";
        String state = "test-state-value";
        OAuthAuthorizationResult authorizationResult = OAuthAuthorizationResult.of(authorizationUrl, state);

        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        Mockito.when(oauthFacade.createOAuthAuthorizationUrl(provider))
                .thenReturn(authorizationResult);

        // when
        oauthController.redirectToProvider(provider, response);

        // then
        verify(oauthFacade).createOAuthAuthorizationUrl(provider);
        verify(response).addHeader(Mockito.eq(HttpHeaders.SET_COOKIE), Mockito.contains("oauthState=" + state));
        verify(response).sendRedirect(authorizationUrl);
    }

    @Test
    @DisplayName("OAuth_콜백에서_state_값이_일치하지_않으면_예외가_발생한다")
    void handleOAuthCallback_stateMismatch_throwsException() {
        // given
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);

        // when, then
        assertThatThrownBy(() ->
                oauthController.handleOAuthCallback("google", "auth-code", "state-from-provider", "different-cookie-state", response))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining(ErrorCode.OAUTH_STATE_INVALID.getMessage());
    }

    @Test
    @DisplayName("OAuth_콜백에서_state_쿠키가_없으면_예외가_발생한다")
    void handleOAuthCallback_missingStateCookie_throwsException() {
        // given
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);

        // when, then
        assertThatThrownBy(() ->
                oauthController.handleOAuthCallback("google", "auth-code", "some-state", null, response))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining(ErrorCode.OAUTH_STATE_INVALID.getMessage());
    }

    @Test
    @DisplayName("OAuth_회원가입에_성공하면_OAuthAuthResponseDto_를_반환한다")
    void signupWithOAuth_returnsLoginResponse() {
        // given
        SignupOAuthRequestDto requestDto = Mockito.mock(SignupOAuthRequestDto.class);

        OAuthResult oauthAuthResult = OAuthResult.builder()
                .accessToken("access-token")
                .refreshToken("refresh-token")
                .build();

        when(userSignupFacade.signupWithOAuth(any())).thenReturn(oauthAuthResult);

        // when
        ResponseEntity<DataApiResponseDto<TokenResponseDto>> response = oauthController.signup(requestDto);

        // then
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCode()).isEqualTo(SuccessCode.LOGIN_SUCCESS.getCode());
        assertThat(response.getBody().getData().getAccessToken()).isEqualTo("access-token");

        HttpHeaders headers = response.getHeaders();
        List<String> cookies = headers.get(HttpHeaders.SET_COOKIE);
        assertThat(cookies).isNotNull();

        verify(userSignupFacade).signupWithOAuth(any());
    }
}
