package teamdevhub.devhub.administration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "teamdevhub.devhub")
public class AdministrationApplication {
    public static void main(String[] args) {
        SpringApplication.run(AdministrationApplication.class, args);
    }
}
