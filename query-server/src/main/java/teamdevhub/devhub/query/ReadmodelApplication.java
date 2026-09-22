package teamdevhub.devhub.query;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"teamdevhub.devhub.query", "teamdevhub.devhub.shared"})
public class ReadmodelApplication {
    public static void main(String[] args) {
        SpringApplication.run(ReadmodelApplication.class, args);
    }
}
