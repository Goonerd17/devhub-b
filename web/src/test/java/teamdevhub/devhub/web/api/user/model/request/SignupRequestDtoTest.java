package teamdevhub.devhub.web.api.user.model.request;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.web.api.terms.model.AgreeTermsRequestDto;
import teamdevhub.devhub.web.api.user.model.SignupRequestDto;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;

class SignupRequestDtoTest {

    private static final String TEST_EMAIL_1 = "user1@example.com";
    private static final String TEST_PASSWORD_1 = "password123!";
    private static final String TEST_USERNAME_1 = "User1";
    private static final String TEST_INTRO_1 = "Hello World";
    private static final List<String> TEST_POSITION_LIST = List.of("001");
    private static final List<String> TEST_SKILL_LIST = List.of("001");

    private Validator validator;

    @BeforeEach
    void init() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("올바른_데이터는_검증에_통과한다")
    void shouldPassForValidData() {
        // given
        SignupRequestDto signupRequestDto = SignupRequestDto.builder()
                .email(TEST_EMAIL_1)
                .password(TEST_PASSWORD_1)
                .username(TEST_USERNAME_1)
                .introduction(TEST_INTRO_1)
                .positionList(TEST_POSITION_LIST)
                .skillList(TEST_SKILL_LIST)
                .termsAgreementList(List.of(
                        AgreeTermsRequestDto.builder()
                                .termsGuid("TERMS1")
                                .agreed(true)
                                .build()
                ))
                .build();

        // when
        Set<ConstraintViolation<SignupRequestDto>> violations = validator.validate(signupRequestDto);

        // then
        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("이메일이_비어있으면_검증에_실패한다")
    void shouldFailWhenEmailIsEmpty() {
        // given
        SignupRequestDto signupRequestDto = SignupRequestDto.builder()
                .email("")
                .password(TEST_PASSWORD_1)
                .introduction(TEST_INTRO_1)
                .positionList(TEST_POSITION_LIST)
                .skillList(TEST_SKILL_LIST)
                .build();

        // when
        Set<ConstraintViolation<SignupRequestDto>> violations = validator.validate(signupRequestDto);

        // then
        assertThat(violations.stream()
                .map(ConstraintViolation::getMessage)
                .toList())
                .contains("이메일은 필수입니다");
    }

    @Test
    @DisplayName("포지션이_없으면_검증에_실패한다")
    void shouldFailWhenPositionIsMissing() {
        // given
        SignupRequestDto signupRequestDto = SignupRequestDto.builder()
                .email(TEST_EMAIL_1)
                .password(TEST_PASSWORD_1)
                .introduction(TEST_INTRO_1)
                .positionList(null)
                .skillList(TEST_SKILL_LIST)
                .build();

        // when
        Set<ConstraintViolation<SignupRequestDto>> violations = validator.validate(signupRequestDto);

        // then
        assertThat(violations.stream()
                .map(ConstraintViolation::getMessage)
                .toList())
                .contains("관심 포지션은 필수입니다");
    }

    @Test
    @DisplayName("포지션이_빈_값이면_검증에_실패한다")
    void shouldFailWhenPositionIsEmpty() {
        // given
        SignupRequestDto signupRequestDto = SignupRequestDto.builder()
                .email(TEST_EMAIL_1)
                .password(TEST_PASSWORD_1)
                .introduction(TEST_INTRO_1)
                .positionList(List.of())
                .skillList(TEST_SKILL_LIST)
                .build();

        // when
        Set<ConstraintViolation<SignupRequestDto>> violations = validator.validate(signupRequestDto);

        // then
        assertThat(violations.stream()
                .map(ConstraintViolation::getMessage)
                .toList())
                .contains("관심 포지션은 최소 1개 이상 선택해야 합니다");
    }

    @Test
    @DisplayName("스킬이_없으면_검증에_실패한다")
    void shouldFailWhenSkillsAreMissing() {
        // given
        SignupRequestDto signupRequestDto = SignupRequestDto.builder()
                .email(TEST_EMAIL_1)
                .password(TEST_PASSWORD_1)
                .introduction(TEST_INTRO_1)
                .positionList(TEST_POSITION_LIST)
                .skillList(null)
                .build();

        // when
        Set<ConstraintViolation<SignupRequestDto>> violations = validator.validate(signupRequestDto);

        // then
        assertThat(violations.stream()
                .map(ConstraintViolation::getMessage)
                .toList())
                .contains("보유 스킬은 필수입니다");
    }

    @Test
    @DisplayName("스킬이_빈_값이면_검증에_실패한다")
    void shouldFailWhenSkillsAreEmpty() {
        // given
        SignupRequestDto signupRequestDto = SignupRequestDto.builder()
                .email(TEST_EMAIL_1)
                .password(TEST_PASSWORD_1)
                .introduction(TEST_INTRO_1)
                .positionList(TEST_POSITION_LIST)
                .skillList(List.of())
                .build();

        // when
        Set<ConstraintViolation<SignupRequestDto>> violations = validator.validate(signupRequestDto);

        // then
        assertThat(violations.stream()
                .map(ConstraintViolation::getMessage)
                .toList())
                .contains("보유 스킬은 최소 1개 이상 선택해야 합니다");
    }
}
