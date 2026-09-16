package teamdevhub.devhub.web.api.user.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import teamdevhub.devhub.web.api.web.validator.RegexMatch;
import teamdevhub.devhub.web.api.web.validator.RegexPattern;

@Schema(description = "관리자 비밀번호 초기화 요청")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminResetPasswordRequestDto {

    @Schema(description = "새 비밀번호 (8~20자, 영문·숫자·특수문자 포함)", example = "NewPass1!")
    @NotBlank(message = "새 비밀번호는 필수입니다")
    @RegexMatch(RegexPattern.AUTH_PASSWORD)
    private String newPassword;
}
