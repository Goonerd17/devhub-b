package teamdevhub.devhub.large;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import teamdevhub.devhub.identity.core.auth.port.out.verification.VerificationCodeProvider;
import teamdevhub.devhub.notification.core.application.selector.NotificationSenderSelector;

@TestConfiguration
public class TestConfig {

    @Bean
    @Primary
    VerificationCodeProvider verificationCodeGenerator() {
        return () -> "123456";
    }

    @Bean
    @Primary
    NotificationSenderSelector notificationSender() {
        return command -> {};
    }
}
