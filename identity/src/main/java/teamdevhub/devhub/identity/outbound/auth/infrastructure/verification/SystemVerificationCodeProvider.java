package teamdevhub.devhub.identity.outbound.auth.infrastructure.verification;

import org.springframework.stereotype.Component;
import teamdevhub.devhub.identity.core.auth.port.out.verification.VerificationCodeProvider;

import java.security.SecureRandom;

@Component
public class SystemVerificationCodeProvider implements VerificationCodeProvider {

    private static final int CODE_LENGTH = 6;
    private static final SecureRandom RANDOM = new SecureRandom();

    @Override
    public String generateVerificationCode() {
        StringBuilder code = new StringBuilder(CODE_LENGTH);
        for (int i = 0; i < CODE_LENGTH; i++) {
            code.append(RANDOM.nextInt(10));
        }
        return code.toString();
    }
}