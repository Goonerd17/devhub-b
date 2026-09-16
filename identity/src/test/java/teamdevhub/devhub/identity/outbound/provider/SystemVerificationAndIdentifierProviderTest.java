package teamdevhub.devhub.identity.outbound.provider;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import teamdevhub.devhub.identity.outbound.auth.infrastructure.verification.SystemVerificationCodeProvider;
import teamdevhub.devhub.platform.identifier.SystemIdentifierProvider;

class SystemVerificationAndIdentifierProviderTest {

    @Test
    void generateEmailVerificationCodeReturns6DigitNumber() {
        SystemVerificationCodeProvider provider = new SystemVerificationCodeProvider();

        String code = provider.generateVerificationCode();

        assertThat(code).isNotNull();
        assertThat(code).hasSize(6);
        assertThat(code).matches("\\d{6}");
    }

    @Test
    void generateIdentifierReturns32CharUUID() {
        SystemIdentifierProvider provider = new SystemIdentifierProvider();

        String id = provider.generateIdentifier();

        assertThat(id).isNotNull();
        assertThat(id).doesNotContain("-");
        assertThat(id.length()).isEqualTo(32);
    }
}
