package teamdevhub.devhub.web.api.auth.model.request;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import teamdevhub.devhub.web.api.auth.model.request.ConfirmVerificationRequestDto;
import teamdevhub.devhub.identity.core.auth.domain.vo.verification.VerificationType;
import teamdevhub.devhub.identity.core.auth.port.in.command.verification.ConfirmVerificationCommand;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class ConfirmVerificationRequestDtoTest {

    private static final String TEST_EMAIL_1 = "user1@example.com";
    private static final String TEST_EMAIL_CODE = "123456";

    private Validator validator;

    @BeforeEach
    void init() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("모든_필드가_올바르면_ConfirmVerificationCommand_로_변환된다")
    void convertConfirmVerificationRequestDtoToCommand() {
        // given
        ConfirmVerificationRequestDto dto = ConfirmVerificationRequestDto.builder()
                .verificationType("email")
                .value(TEST_EMAIL_1)
                .code(TEST_EMAIL_CODE)
                .build();

        // when
        ConfirmVerificationCommand confirmVerificationCommand = dto.toConfirmVerificationCommand();

        // then
        assertThat(confirmVerificationCommand.verificationTarget().verificationType()).isEqualTo(VerificationType.EMAIL);
        assertThat(confirmVerificationCommand.verificationTarget().value()).isEqualTo(TEST_EMAIL_1);
        assertThat(confirmVerificationCommand.code()).isEqualTo(TEST_EMAIL_CODE);
    }

    @Test
    @DisplayName("필드가_null_이거나_빈값이면_검증에_실패한다")
    void validationFailsForConfirmVerificationRequestDto() {
        // given
        ConfirmVerificationRequestDto dto = new ConfirmVerificationRequestDto();

        // when
        Set<ConstraintViolation<ConfirmVerificationRequestDto>> violations = validator.validate(dto);

        // then
        assertThat(violations).hasSize(3);
        assertThat(violations.stream().map(ConstraintViolation::getMessage).toList())
                .contains("공백일 수 없습니다", "공백일 수 없습니다", "널이어서는 안됩니다");
    }
}
