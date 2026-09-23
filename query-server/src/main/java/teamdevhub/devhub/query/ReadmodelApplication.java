package teamdevhub.devhub.query;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = {"teamdevhub.devhub.query", "teamdevhub.devhub.shared"})
@EnableFeignClients
public class ReadmodelApplication {
    public static void main(String[] args) {
        SpringApplication.run(ReadmodelApplication.class, args);
    }
}
