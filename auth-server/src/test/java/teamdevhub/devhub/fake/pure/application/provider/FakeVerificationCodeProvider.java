package teamdevhub.devhub.fake.pure.application.provider;

import teamdevhub.devhub.auth.core.auth.port.out.verification.VerificationCodeProvider;

public class FakeVerificationCodeProvider implements VerificationCodeProvider {

    private final String fixedEmailVerificationCode;

    public FakeVerificationCodeProvider(String fixedEmailVerificationCode) {
        this.fixedEmailVerificationCode = fixedEmailVerificationCode;
    }

    @Override
    public String generateVerificationCode() {
        return fixedEmailVerificationCode;
    }
}
