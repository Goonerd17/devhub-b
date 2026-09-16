package teamdevhub.devhub.medium.shared.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static teamdevhub.devhub.constant.UserTestConstant.TEST_PASSWORD_1;

public class PasswordCryptoConfigTest {

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Test
    @DisplayName("비밀번호를_인코딩하면_원본과_일치여부를_검증할_수_있다")
    void matchesEncodedPassword() {
        // given
        String rawPassword = TEST_PASSWORD_1;

        // when
        String encoded = passwordEncoder.encode(rawPassword);

        // then
        assertThat(encoded).isNotNull();
        assertThat(passwordEncoder.matches(rawPassword, encoded)).isTrue();
        assertThat(passwordEncoder.matches("wrongPassword", encoded)).isFalse();
    }
}
