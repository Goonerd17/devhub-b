package teamdevhub.devhub.media;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import teamdevhub.devhub.media.outbound.infrastructure.FileStorageProperties;

@SpringBootApplication(scanBasePackages = "teamdevhub.devhub")
@EnableConfigurationProperties(FileStorageProperties.class)
public class MediaApplication {
    public static void main(String[] args) {
        SpringApplication.run(MediaApplication.class, args);
    }
}
