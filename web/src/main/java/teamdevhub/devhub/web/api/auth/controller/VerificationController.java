package teamdevhub.devhub.web.api.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import teamdevhub.devhub.web.api.auth.model.request.ConfirmVerificationRequestDto;
import teamdevhub.devhub.web.api.auth.model.request.IssueVerificationRequestDto;
import teamdevhub.devhub.identity.core.auth.port.in.facade.VerificationFacade;
import teamdevhub.devhub.web.shared.enums.SuccessCode;
import teamdevhub.devhub.web.api.web.model.response.DataApiResponseDto;

@Tag(name = "Verification", description = "이메일 인증 API")
@RestController
@RequestMapping("/auth/verification")
@RequiredArgsConstructor
public class VerificationController {

    private final VerificationFacade verificationFacade;

    @Operation(summary = "이메일 인증 코드 발송", description = "입력한 이메일 주소로 인증 코드를 발송합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "인증 코드 발송 성공"),
            @ApiResponse(responseCode = "400", description = "유효하지 않은 이메일 형식")
    })
    @PostMapping("/email")
    public ResponseEntity<DataApiResponseDto<Void>> sendEmailVerification(@Valid @RequestBody IssueVerificationRequestDto issueVerificationRequestDto) {
        verificationFacade.issueVerification(issueVerificationRequestDto.toIssueVerificationCommand());
        return ResponseEntity.ok(
                DataApiResponseDto.successWithoutData(
                        SuccessCode.VERIFICATION_SENT
                )
        );
    }

    @Operation(summary = "이메일 인증 코드 확인", description = "발송된 인증 코드를 입력하여 이메일 인증을 완료합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "이메일 인증 성공"),
            @ApiResponse(responseCode = "400", description = "인증 코드 불일치 또는 만료")
    })
    @PostMapping("/email/confirm")
    public ResponseEntity<DataApiResponseDto<Void>> confirmEmailVerification(@Valid @RequestBody ConfirmVerificationRequestDto confirmVerificationRequestDto) {
        verificationFacade.confirmVerification(confirmVerificationRequestDto.toConfirmVerificationCommand());
        return ResponseEntity.ok(
                DataApiResponseDto.successWithoutData(
                        SuccessCode.VERIFICATION_SUCCESS
                )
        );
    }
}
