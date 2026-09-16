package teamdevhub.devhub.web.api.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import teamdevhub.devhub.web.api.auth.model.request.LoginRequestDto;
import teamdevhub.devhub.web.api.user.model.UpdatePasswordRequestDto;
import teamdevhub.devhub.identity.core.auth.application.service.AuthResult;

import teamdevhub.devhub.web.api.auth.model.response.TokenResponseDto;
import teamdevhub.devhub.web.api.web.model.response.DataApiResponseDto;
import teamdevhub.devhub.web.api.web.resolver.LoginUser;
import teamdevhub.devhub.identity.core.auth.domain.vo.user.AuthenticatedUser;
import teamdevhub.devhub.web.shared.enums.SuccessCode;
import teamdevhub.devhub.identity.core.auth.port.in.facade.AuthFacade;

@Tag(name = "Auth", description = "이메일/비밀번호 인증 API")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthFacade authFacade;
    private final CookieFactory cookieFactory;

    @Operation(summary = "로그인", description = "이메일과 비밀번호로 로그인합니다. 응답 헤더에 Access Token, 쿠키에 Refresh Token이 설정됩니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "로그인 성공"),
            @ApiResponse(responseCode = "400", description = "이메일/비밀번호 유효성 오류"),
            @ApiResponse(responseCode = "401", description = "인증 실패")
    })
    @PostMapping("/login")
    public ResponseEntity<DataApiResponseDto<TokenResponseDto>> login(@Valid @RequestBody LoginRequestDto loginRequestDto) {
        AuthResult authResult = authFacade.login(loginRequestDto.toLoginCommand());
        ResponseCookie refreshCookie = cookieFactory.createRefreshTokenCookie(authResult.refreshToken());
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, authResult.toAuthorizationHeader())
                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                .body(DataApiResponseDto.successWithData(
                        SuccessCode.LOGIN_SUCCESS,
                        TokenResponseDto.issueAccessToken(authResult.accessToken()))
                );
    }

    @Operation(summary = "토큰 재발급", description = "HTTP-only 쿠키의 Refresh Token으로 Access Token을 재발급합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "재발급 성공"),
            @ApiResponse(responseCode = "401", description = "유효하지 않은 Refresh Token")
    })
    @PostMapping("/reissue")
    public ResponseEntity<DataApiResponseDto<TokenResponseDto>> refresh(
            @Parameter(description = "HTTP-only 쿠키로 전달되는 Refresh Token", required = true)
            @CookieValue("refreshToken") String refreshToken) {
        AuthResult authResult = authFacade.reissueAccessToken(refreshToken);
        ResponseCookie newRefreshCookie = cookieFactory.createRefreshTokenCookie(authResult.refreshToken());
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION, authResult.toAuthorizationHeader())
                .header(HttpHeaders.SET_COOKIE, newRefreshCookie.toString())
                .body(DataApiResponseDto.successWithData(
                        SuccessCode.CREATE_SUCCESS,
                        TokenResponseDto.issueAccessToken(authResult.accessToken())
                ));
    }

    @Operation(summary = "로그아웃", description = "현재 로그인된 사용자의 Refresh Token을 무효화하고 로그아웃합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "로그아웃 성공"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자")
    })
    @PostMapping("/logout")
    public ResponseEntity<DataApiResponseDto<Void>> logout(@LoginUser AuthenticatedUser authenticatedUser, HttpServletResponse httpServletResponse) {
        authFacade.logout(authenticatedUser.userGuid());
        httpServletResponse.addHeader(HttpHeaders.SET_COOKIE, cookieFactory.expireRefreshTokenCookie().toString());
        return ResponseEntity.ok(
                DataApiResponseDto.successWithoutData(
                        SuccessCode.LOGOUT_SUCCESS
                )
        );
    }

    @Operation(summary = "비밀번호 변경", description = "로그인된 사용자의 비밀번호를 변경합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "비밀번호 변경 성공"),
            @ApiResponse(responseCode = "400", description = "유효성 검사 실패"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자")
    })
    @PutMapping("/password")
    public ResponseEntity<DataApiResponseDto<Void>> updatePassword(@Valid @RequestBody UpdatePasswordRequestDto updatePasswordRequestDto, @LoginUser AuthenticatedUser authenticatedUser) {
        authFacade.updatePassword(updatePasswordRequestDto.toUpdatePasswordCommand(authenticatedUser.userGuid()));
        return ResponseEntity.ok(
                DataApiResponseDto.successWithoutData(
                        SuccessCode.UPDATE_SUCCESS
                )
        );
    }
}
