package teamdevhub.devhub.auth.http.auth.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import teamdevhub.devhub.auth.http.validator.RegexPattern;
import teamdevhub.devhub.auth.http.validator.RegexMatch;
import teamdevhub.devhub.auth.core.auth.port.in.command.LoginCommand;

@Schema(description = "로그인 요청")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequestDto {

    @Schema(description = "이메일 주소", example = "user@example.com")
    @NotBlank(message = "이메일은 필수입니다")
    @RegexMatch(RegexPattern.AUTH_EMAIL)
    private String email;

    @Schema(description = "비밀번호 (8~20자, 영문·숫자·특수문자 포함)", example = "Password1!")
    @NotBlank(message = "비밀번호는 필수입니다")
    @RegexMatch(RegexPattern.AUTH_PASSWORD)
    private String password;

    public LoginCommand toLoginCommand() {
        return LoginCommand.builder()
                .email(this.email)
                .password(this.password)
                .build();
    }
}
