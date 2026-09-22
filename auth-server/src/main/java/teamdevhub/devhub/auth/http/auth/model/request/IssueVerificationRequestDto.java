package teamdevhub.devhub.auth.http.auth.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import teamdevhub.devhub.auth.core.auth.domain.vo.verification.VerificationTarget;
import teamdevhub.devhub.auth.core.auth.domain.vo.verification.VerificationType;
import teamdevhub.devhub.auth.core.auth.port.in.command.verification.IssueVerificationCommand;

@Schema(description = "이메일 인증 코드 발송 요청")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IssueVerificationRequestDto {

    @Schema(description = "인증 유형 (EMAIL)", example = "EMAIL")
    @NotNull
    private String verificationType;

    @Schema(description = "인증 대상 이메일 주소", example = "user@example.com")
    @NotBlank
    private String value;

    public IssueVerificationCommand toIssueVerificationCommand() {
        VerificationType verificationType = VerificationType.from(this.verificationType);
        return new IssueVerificationCommand(VerificationTarget.of(verificationType, value));
    }
}

