package teamdevhub.devhub.web.api.auth.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import teamdevhub.devhub.web.api.auth.controller.AuthController;
import teamdevhub.devhub.web.api.auth.model.request.LoginRequestDto;
import teamdevhub.devhub.web.api.auth.model.response.TokenResponseDto;
import teamdevhub.devhub.web.api.web.model.response.DataApiResponseDto;
import teamdevhub.devhub.identity.core.auth.application.service.AuthResult;
import teamdevhub.devhub.identity.core.auth.domain.vo.user.AuthenticatedUser;
import teamdevhub.devhub.web.shared.enums.SuccessCode;
import teamdevhub.devhub.member.api.MemberRole;
import teamdevhub.devhub.web.api.auth.controller.CookieFactory;
import teamdevhub.devhub.identity.core.auth.port.in.facade.AuthFacade;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static teamdevhub.devhub.constant.UserTestConstant.*;

class AuthControllerTest {

    private AuthController authController;

    private AuthFacade authFacade;

    @BeforeEach
    void init() {
        authFacade = Mockito.mock(AuthFacade.class);

        authController = new AuthController(authFacade, new CookieFactory());
    }

    @Test
    @DisplayName("濡쒓렇?몄뿉_?깃났?섎㈃_LOGIN_SUCCESS_肄붾뱶瑜??뺤씤?????덈떎")
    void canVerifyCodeWhenLoginSucceed() {
        // given
        LoginRequestDto loginRequestDto = LoginRequestDto.builder()
                .email(TEST_EMAIL_1)
                .password(TEST_PASSWORD_1)
                .build();

        AuthResult authResult = AuthResult.builder()
                .accessToken("access-token")
                .refreshToken("refresh-token")
                .build();

        when(authFacade.login(any())).thenReturn(authResult);

        // when
        ResponseEntity<DataApiResponseDto<TokenResponseDto>> response = authController.login(loginRequestDto);

        // then
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCode()).isEqualTo(SuccessCode.LOGIN_SUCCESS.getCode());

        HttpHeaders headers = response.getHeaders();
        List<String> cookies = headers.get(HttpHeaders.SET_COOKIE);
        assertThat(cookies).isNotNull();

        verify(authFacade).login(any());
    }

    @Test
    @DisplayName("?좏겙_?щ컻湲됱뿉_?깃났?섎㈃_CREATE_SUCCESS_肄붾뱶?_??由ы봽?덉떆_荑좏궎瑜??뺤씤?????덈떎")
    void canVerifyCodeWhenRefreshingToken() {
        // given
        AuthResult authResult = AuthResult.of("new-access-token", "new-refresh-token");
        String oldRefreshToken = "old-refresh-token";
        when(authFacade.reissueAccessToken(oldRefreshToken)).thenReturn(authResult);

        // when
        ResponseEntity<DataApiResponseDto<TokenResponseDto>> response = authController.refresh(oldRefreshToken);

        // then
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCode()).isEqualTo(SuccessCode.CREATE_SUCCESS.getCode());

        List<String> cookies = response.getHeaders().get(HttpHeaders.SET_COOKIE);
        assertThat(cookies).isNotNull();
        assertThat(cookies).anyMatch(c -> c.contains("new-refresh-token"));

        verify(authFacade).reissueAccessToken(oldRefreshToken);
    }

    @Test
    @DisplayName("濡쒓렇?꾩썐???깃났?섎㈃_LOGOUT_SUCCESS_肄붾뱶瑜??뺤씤?????덈떎")
    void canVerifyCodeWhenLogoutSucceed() {
        // given
        AuthenticatedUser authenticatedUser = new AuthenticatedUser(
                TEST_USER_GUID_1,
                TEST_EMAIL_1,
                MemberRole.USER
        );

        HttpServletResponse response = mock(HttpServletResponse.class);

        doNothing().when(authFacade).logout(TEST_USER_GUID_1);

        // when
        ResponseEntity<DataApiResponseDto<Void>> result =
                authController.logout(authenticatedUser, response);

        // then
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getCode())
                .isEqualTo(SuccessCode.LOGOUT_SUCCESS.getCode());

        verify(authFacade).logout(TEST_USER_GUID_1);
        verify(response).addHeader(
                eq(HttpHeaders.SET_COOKIE),
                anyString()
        );
    }
}