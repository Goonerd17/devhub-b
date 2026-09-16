package teamdevhub.devhub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@ComponentScan(basePackages = "teamdevhub.devhub",
		excludeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = SpringBootConfiguration.class))
@EnableJpaAuditing(auditorAwareRef = "auditorAwareProvider")
@ConfigurationPropertiesScan
public class DevhubApplication {

	public static void main(String[] args) {
		SpringApplication.run(DevhubApplication.class, args);
	}
}
