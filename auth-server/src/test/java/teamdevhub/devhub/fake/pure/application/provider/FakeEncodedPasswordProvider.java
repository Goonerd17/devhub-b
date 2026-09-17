package teamdevhub.devhub.fake.pure.application.provider;

import teamdevhub.devhub.auth.core.auth.port.out.password.EncodedPasswordProvider;

public class FakeEncodedPasswordProvider implements EncodedPasswordProvider {

    private static final String PREFIX = "ENC.";

    @Override
    public String encode(String rawPassword) {
        return PREFIX + rawPassword;
    }

    @Override
    public boolean matches(String rawPassword, String hashedPassword) {
        return hashedPassword.equals(PREFIX + rawPassword);
    }
}
