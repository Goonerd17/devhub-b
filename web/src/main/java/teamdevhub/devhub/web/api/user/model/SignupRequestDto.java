package teamdevhub.devhub.web.api.user.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import teamdevhub.devhub.web.api.terms.model.AgreeTermsRequestDto;
import teamdevhub.devhub.web.api.web.validator.RegexPattern;
import teamdevhub.devhub.web.api.web.validator.RegexMatch;
import teamdevhub.devhub.identity.core.auth.domain.vo.verification.VerificationTarget;
import teamdevhub.devhub.identity.core.auth.domain.vo.verification.VerificationType;
import teamdevhub.devhub.identity.core.auth.port.in.command.LoginCommand;
import teamdevhub.devhub.identity.core.user.port.in.command.SignupUserCommand;

import java.util.List;

@Schema(description = "이메일 회원가입 요청")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignupRequestDto {

    @Schema(description = "이메일 주소", example = "user@example.com")
    @NotBlank(message = "이메일은 필수입니다")
    @RegexMatch(RegexPattern.AUTH_EMAIL)
    private String email;

    @Schema(description = "비밀번호 (8~20자, 영문·숫자·특수문자 포함)", example = "Password1!")
    @NotBlank(message = "비밀번호는 필수입니다")
    @RegexMatch(RegexPattern.AUTH_PASSWORD)
    private String password;

    @Schema(description = "사용자 닉네임", example = "devHunter")
    @NotBlank(message = "사용자명은 필수입니다")
    @RegexMatch(RegexPattern.USERNAME)
    private String username;

    @Schema(description = "자기소개", example = "안녕하세요, 백엔드 개발자입니다.")
    private String introduction;

    @Schema(description = "관심 포지션 목록 (최소 1개)", example = "[\"백엔드\", \"프론트엔드\"]")
    @NotNull(message = "관심 포지션은 필수입니다")
    @Size(min = 1, message = "관심 포지션은 최소 1개 이상 선택해야 합니다")
    private List<@NotBlank String> positionList;

    @Schema(description = "보유 기술 스택 목록 (최소 1개)", example = "[\"Java\", \"Spring\"]")
    @NotNull(message = "보유 스킬은 필수입니다")
    @Size(min = 1, message = "보유 스킬은 최소 1개 이상 선택해야 합니다")
    private List<@NotBlank String> skillList;

    @Schema(description = "약관 동의 목록 (최소 1개)")
    @NotNull(message = "약관 동의 정보는 필수입니다")
    @Size(min = 1, message = "약관 동의는 최소 1개 이상 필요합니다")
    private List<AgreeTermsRequestDto> termsAgreementList;

    public SignupUserCommand toSignupCommand() {
        return SignupUserCommand.builder()
                .email(this.email)
                .password(this.password)
                .username(this.username)
                .introduction(this.introduction)
                .positionList(this.positionList)
                .skillList(this.skillList)
                .termsAgreementItemList(
                        this.termsAgreementList.stream()
                                .map(AgreeTermsRequestDto::toTermsAgreementItem)
                                .toList())
                .verificationTarget(VerificationTarget.of(VerificationType.EMAIL, this.email))
                .build();
    }

    public LoginCommand toLoginCommand() {
        return LoginCommand.builder()
                .email(this.email)
                .password(this.password)
                .build();
    }
}