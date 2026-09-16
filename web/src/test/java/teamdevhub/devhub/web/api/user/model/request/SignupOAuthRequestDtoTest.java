package teamdevhub.devhub.web.api.user.model.request;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import teamdevhub.devhub.web.api.terms.model.AgreeTermsRequestDto;
import teamdevhub.devhub.web.api.user.model.SignupOAuthRequestDto;
import teamdevhub.devhub.identity.core.auth.port.in.command.oauth.SignupOAuthUserCommand;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class SignupOAuthRequestDtoTest {

    private Validator validator;

    @BeforeEach
    void init() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("모든_필드가_올바르면_OAuthSignupCommand_로_변환된다")
    void convertOAuthSignupRequestDtoToSignupOAuthUserCommand() {
        // given
        SignupOAuthRequestDto signupOAuthRequestDto = SignupOAuthRequestDto.builder()
                .tempToken("temp-token-123")
                .username("testUser")
                .introduction("안녕하세요")
                .positionList(List.of("BACKEND"))
                .skillList(List.of("JAVA", "SPRING"))
                .termsAgreementList(List.of(
                        AgreeTermsRequestDto.builder()
                                .termsGuid("TERMS1")
                                .agreed(true)
                                .build()
                ))
                .build();

        // when
        SignupOAuthUserCommand signupOAuthUserCommand = signupOAuthRequestDto.toSignupOAuthUserCommand();

        // then
        assertThat(signupOAuthUserCommand.tempToken()).isEqualTo("temp-token-123");
        assertThat(signupOAuthUserCommand.username()).isEqualTo("testUser");
        assertThat(signupOAuthUserCommand.introduction()).isEqualTo("안녕하세요");
        assertThat(signupOAuthUserCommand.positionList()).containsExactly("BACKEND");
        assertThat(signupOAuthUserCommand.skillList()).containsExactly("JAVA", "SPRING");
    }

    @Test
    @DisplayName("필수_필드가_null_이거나_빈값이면_검증에_실패한다")
    void validationFailsForOAuthSignupRequestDto() {
        // given
        SignupOAuthRequestDto signupOAuthRequestDto = new SignupOAuthRequestDto();

        // when
        Set<ConstraintViolation<SignupOAuthRequestDto>> violations = validator.validate(signupOAuthRequestDto);

        // then
        assertThat(violations).isNotEmpty();

        assertThat(violations.stream()
                .map(ConstraintViolation::getMessage)
                .toList())
                .contains("TEMP 토큰은 필수입니다",
                        "관심 포지션은 필수입니다",
                        "보유 스킬은 필수입니다");
    }

    @Test
    @DisplayName("positionList_와_skillList_가_빈_리스트면_검증에_실패한다")
    void validationFailsWhenListsAreEmpty() {
        // given
        SignupOAuthRequestDto signupOAuthRequestDto = SignupOAuthRequestDto.builder()
                .tempToken("temp-token-123")
                .positionList(List.of())
                .skillList(List.of())
                .build();

        // when
        Set<ConstraintViolation<SignupOAuthRequestDto>> violations = validator.validate(signupOAuthRequestDto);

        // then
        assertThat(violations.stream()
                .map(ConstraintViolation::getMessage)
                .toList())
                .contains("관심 포지션은 최소 1개 이상 선택해야 합니다",
                        "보유 스킬은 최소 1개 이상 선택해야 합니다");
    }
}
