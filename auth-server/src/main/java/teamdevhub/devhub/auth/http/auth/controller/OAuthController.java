package teamdevhub.devhub.auth.http.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import teamdevhub.devhub.auth.http.auth.model.response.TokenResponseDto;
import teamdevhub.devhub.auth.http.user.model.SignupOAuthRequestDto;
import teamdevhub.devhub.auth.core.auth.application.service.oauth.vo.OAuthResult;
import teamdevhub.devhub.auth.core.auth.application.service.oauth.vo.OAuthAuthorizationResult;
import teamdevhub.devhub.web.api.model.response.DataApiResponseDto;
import teamdevhub.devhub.auth.core.auth.application.service.oauth.SignupStatus;
import teamdevhub.devhub.auth.core.auth.port.in.facade.OAuthFacade;
import teamdevhub.devhub.shared.core.common.exception.BusinessRuleException;
import teamdevhub.devhub.shared.enums.ErrorCode;
import teamdevhub.devhub.web.enums.SuccessCode;
import teamdevhub.devhub.auth.core.user.port.in.facade.UserSignupFacade;

import java.io.IOException;

@Tag(name = "OAuth", description = "소셜 로그인(Google·GitHub·Kakao·Naver) API")
@RestController
@RequestMapping("/auth/oauth")
@RequiredArgsConstructor
public class OAuthController {

    private final UserSignupFacade userSignupFacade;
    private final OAuthFacade oauthFacade;
    private final CookieFactory cookieFactory;

    @Value("${app.frontend.base-url}")
    private String frontendBaseUrl;

    @Operation(summary = "OAuth 인가 URL 리다이렉트", description = "지정된 OAuth 제공자의 로그인 페이지로 리다이렉트합니다. (google, github, kakao, naver)")
    @ApiResponse(responseCode = "302", description = "OAuth 제공자 인가 페이지로 리다이렉트")
    @GetMapping("/{provider}")
    public void redirectToProvider(
            @Parameter(description = "OAuth 제공자 (google, github, kakao, naver)", example = "google", required = true)
            @PathVariable String provider,
            HttpServletResponse httpServletResponse) throws IOException {
        OAuthAuthorizationResult result = oauthFacade.createOAuthAuthorizationUrl(provider);
        ResponseCookie stateCookie = cookieFactory.createOAuthStateCookie(result.state());
        httpServletResponse.addHeader(HttpHeaders.SET_COOKIE, stateCookie.toString());
        httpServletResponse.sendRedirect(result.url());
    }

    @Operation(summary = "OAuth 콜백 처리", description = "OAuth 제공자로부터 인가 코드를 받아 로그인 또는 회원가입 흐름을 처리합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "302", description = "로그인 완료 시 메인 페이지로, 미가입 시 회원가입 페이지로 리다이렉트")
    })
    @GetMapping("/{provider}/callback")
    public void handleOAuthCallback(
            @Parameter(description = "OAuth 제공자", example = "google", required = true) @PathVariable String provider,
            @Parameter(description = "OAuth 제공자로부터 받은 인가 코드", required = true) @RequestParam String code,
            @Parameter(description = "OAuth state 값") @RequestParam String state,
            @CookieValue(value = "oauthState", required = false) String cookieState,
            HttpServletResponse httpServletResponse) throws IOException {
        if (cookieState == null || !cookieState.equals(state)) {
            throw BusinessRuleException.of(ErrorCode.OAUTH_STATE_INVALID);
        }

        ResponseCookie expiredStateCookie = cookieFactory.expireOAuthStateCookie();
        httpServletResponse.addHeader(HttpHeaders.SET_COOKIE, expiredStateCookie.toString());

        OAuthResult oauthResult = oauthFacade.handleOAuthCallback(provider, code);

        if (oauthResult.signupStatus().equals(SignupStatus.COMPLETED)) {
            ResponseCookie refreshCookie = cookieFactory.createRefreshTokenCookie(oauthResult.refreshToken());
            httpServletResponse.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());
            httpServletResponse.sendRedirect(frontendBaseUrl + "/?oauth=true");
        } else {
            httpServletResponse.sendRedirect(frontendBaseUrl + "/auth/signup?token=" + oauthResult.tempToken());
        }
    }

    @Operation(summary = "OAuth 회원가입 완료", description = "임시 토큰과 추가 프로필 정보를 이용하여 OAuth 회원가입을 완료합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "회원가입 성공, Access Token 반환"),
            @ApiResponse(responseCode = "400", description = "유효하지 않은 임시 토큰 또는 요청 데이터")
    })
    @PostMapping("/signup")
    public ResponseEntity<DataApiResponseDto<TokenResponseDto>> signup(@Valid @RequestBody SignupOAuthRequestDto signupOAuthRequestDto) {
        OAuthResult oauthResult = userSignupFacade.signupWithOAuth(signupOAuthRequestDto.toSignupOAuthUserCommand());
        ResponseCookie refreshCookie = cookieFactory.createRefreshTokenCookie(oauthResult.refreshToken());
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, oauthResult.toauthorizationHeader())
                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                .body(DataApiResponseDto.successWithData(
                        SuccessCode.LOGIN_SUCCESS,
                        TokenResponseDto.issueAccessToken(oauthResult.accessToken()))
                );
    }
}
