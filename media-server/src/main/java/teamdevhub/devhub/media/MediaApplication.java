package teamdevhub.devhub.media;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;
import teamdevhub.devhub.media.outbound.infrastructure.FileStorageProperties;
import teamdevhub.devhub.shared.identifier.SystemIdentifierProvider;

@SpringBootApplication
@EnableConfigurationProperties(FileStorageProperties.class)
@Import(SystemIdentifierProvider.class)
public class MediaApplication {
    public static void main(String[] args) {
        SpringApplication.run(MediaApplication.class, args);
    }
}
