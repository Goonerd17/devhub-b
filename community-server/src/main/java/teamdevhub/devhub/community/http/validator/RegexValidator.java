package teamdevhub.devhub.community.http.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class RegexValidator implements ConstraintValidator<RegexMatch, String> {

    private RegexPattern regexPattern;

    @Override
    public void initialize(RegexMatch constraintAnnotation) {
        this.regexPattern = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {

        if (value == null) return true;

        boolean matched = value.matches(regexPattern.regexp());

        if (!matched) {
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext
                    .buildConstraintViolationWithTemplate(regexPattern.message())
                    .addConstraintViolation();
        }

        return matched;
    }
}

