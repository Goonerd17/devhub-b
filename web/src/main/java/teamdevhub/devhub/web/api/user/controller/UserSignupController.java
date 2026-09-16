package teamdevhub.devhub.web.api.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import teamdevhub.devhub.web.api.auth.controller.CookieFactory;
import teamdevhub.devhub.web.api.auth.model.response.TokenResponseDto;
import teamdevhub.devhub.web.api.user.model.SignupRequestDto;
import teamdevhub.devhub.web.api.web.model.response.DataApiResponseDto;
import teamdevhub.devhub.identity.core.auth.application.service.AuthResult;
import teamdevhub.devhub.web.shared.enums.SuccessCode;
import teamdevhub.devhub.web.core.user.port.in.facade.UserSignupFacade;

@Tag(name = "User - Signup", description = "이메일 회원가입 API")
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserSignupController {

    private final UserSignupFacade userSignupFacade;
    private final CookieFactory cookieFactory;

    @Operation(summary = "이메일 회원가입", description = "이메일, 비밀번호, 프로필 정보, 약관 동의 정보를 입력하여 회원가입합니다. 가입 후 자동 로그인되어 토큰이 반환됩니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "회원가입 및 로그인 성공, Access Token 반환"),
            @ApiResponse(responseCode = "400", description = "유효성 검사 실패 또는 이미 존재하는 이메일")
    })
    @PostMapping("/signup")
    public ResponseEntity<DataApiResponseDto<TokenResponseDto>> signup(@Valid @RequestBody SignupRequestDto signupRequestDto) {
        AuthResult authResult = userSignupFacade.signup(signupRequestDto.toSignupCommand());
        ResponseCookie refreshCookie = cookieFactory.createRefreshTokenCookie(authResult.refreshToken());
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, authResult.toAuthorizationHeader())
                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                .body(DataApiResponseDto.successWithData(
                        SuccessCode.LOGIN_SUCCESS,
                        TokenResponseDto.issueAccessToken(authResult.accessToken()))
                );
    }
}
