package teamdevhub.devhub.web.api.auth.model.request;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import teamdevhub.devhub.web.api.auth.model.request.IssueVerificationRequestDto;
import teamdevhub.devhub.identity.core.auth.domain.vo.verification.VerificationType;
import teamdevhub.devhub.identity.core.auth.port.in.command.verification.IssueVerificationCommand;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class IssueVerificationRequestDtoTest {

    private static final String TEST_EMAIL_1 = "user1@example.com";

    private Validator validator;

    @BeforeEach
    void init() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("모든_필드가_올바르면_IssueVerificationCommand_로_변환된다")
    void convertIssueVerificationRequestDtoToCommand() {
        // given
        IssueVerificationRequestDto issueVerificationRequestDto = IssueVerificationRequestDto.builder()
                .verificationType("email")
                .value(TEST_EMAIL_1)
                .build();

        // when
        IssueVerificationCommand issueVerificationCommand = issueVerificationRequestDto.toIssueVerificationCommand();

        // then
        assertThat(issueVerificationCommand.verificationTarget().verificationType()).isEqualTo(VerificationType.EMAIL);
        assertThat(issueVerificationCommand.verificationTarget().value()).isEqualTo(TEST_EMAIL_1);
    }

    @Test
    @DisplayName("필드가_null_이거나_빈값이면_검증에_실패한다")
    void validationFailsForIssueVerificationRequestDto() {
        // given
        IssueVerificationRequestDto issueVerificationRequestDto = new IssueVerificationRequestDto();

        // when
        Set<ConstraintViolation<IssueVerificationRequestDto>> violations = validator.validate(issueVerificationRequestDto);

        // then
        assertThat(violations).hasSize(2);
        assertThat(violations.stream().map(ConstraintViolation::getMessage).toList())
                .contains("공백일 수 없습니다", "공백일 수 없습니다", "널이어서는 안됩니다");
    }
}
