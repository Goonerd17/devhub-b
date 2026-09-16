package teamdevhub.devhub.web.api.user.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import teamdevhub.devhub.web.api.terms.model.AgreeTermsRequestDto;
import teamdevhub.devhub.web.api.web.validator.RegexMatch;
import teamdevhub.devhub.web.api.web.validator.RegexPattern;
import teamdevhub.devhub.identity.core.auth.port.in.command.oauth.SignupOAuthUserCommand;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignupOAuthRequestDto {

    @NotBlank(message = "TEMP 토큰은 필수입니다")
    private String tempToken;

    @RegexMatch(RegexPattern.USERNAME)
    private String username;

    private String introduction;

    @NotNull(message = "관심 포지션은 필수입니다")
    @Size(min = 1, message = "관심 포지션은 최소 1개 이상 선택해야 합니다")
    private List<@NotBlank String> positionList;

    @NotNull(message = "보유 스킬은 필수입니다")
    @Size(min = 1, message = "보유 스킬은 최소 1개 이상 선택해야 합니다")
    private List<@NotBlank String> skillList;

    @NotNull(message = "약관 동의 정보는 필수입니다")
    @Size(min = 1, message = "약관 동의는 최소 1개 이상 필요합니다")
    private List<AgreeTermsRequestDto> termsAgreementList;

    public SignupOAuthUserCommand toSignupOAuthUserCommand() {
        return SignupOAuthUserCommand.builder()
                .tempToken(this.tempToken)
                .username(this.username)
                .introduction(this.introduction)
                .positionList(this.positionList)
                .skillList(this.skillList)
                .termsAgreementItemList(
                        this.termsAgreementList.stream()
                                .map(AgreeTermsRequestDto::toTermsAgreementItem)
                                .toList())
                .build();
    }
}
