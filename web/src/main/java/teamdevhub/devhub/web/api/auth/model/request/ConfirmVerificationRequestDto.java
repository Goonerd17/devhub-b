package teamdevhub.devhub.web.api.auth.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import teamdevhub.devhub.identity.core.auth.domain.vo.verification.VerificationTarget;
import teamdevhub.devhub.identity.core.auth.domain.vo.verification.VerificationType;
import teamdevhub.devhub.identity.core.auth.port.in.command.verification.ConfirmVerificationCommand;

@Schema(description = "이메일 인증 코드 확인 요청")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConfirmVerificationRequestDto {

    @Schema(description = "인증 유형 (EMAIL)", example = "EMAIL")
    @NotNull
    private String verificationType;

    @Schema(description = "인증 대상 이메일 주소", example = "user@example.com")
    @NotBlank
    private String value;

    @Schema(description = "이메일로 수신한 인증 코드 6자리", example = "123456")
    @NotBlank
    private String code;

    public ConfirmVerificationCommand toConfirmVerificationCommand() {
        VerificationType verificationType = VerificationType.from(this.verificationType);
        return new ConfirmVerificationCommand(VerificationTarget.of(verificationType, value), code);
    }
}
