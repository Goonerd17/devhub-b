package teamdevhub.devhub.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = {"teamdevhub.devhub.admin", "teamdevhub.devhub.shared"})
@EnableFeignClients
public class AdministrationApplication {
    public static void main(String[] args) {
        SpringApplication.run(AdministrationApplication.class, args);
    }
}
