package teamdevhub.devhub.bootstrap.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI jwtOpenAPI() {
        final String securitySchemeName = "BearerAuth";
        return new OpenAPI()
                .info(new Info()
                        .title("DevHub API")
                        .description("""
                                DevHub API 문서입니다.
                                인증이 필요한 API는 우측 상단 Authorize 버튼을 눌러 Bearer Token 을 입력하세요.
                                """)
                        .version("v1.0.0"))
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(new Components()
                        .addSecuritySchemes(
                                securitySchemeName,
                                new SecurityScheme()
                                        .name(securitySchemeName)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT"))
                );
    }

    @Bean
    public GroupedOpenApi authGroup() {
        return GroupedOpenApi.builder()
                .group("Auth")
                .pathsToMatch("/auth/**")
                .build();
    }

    @Bean
    public GroupedOpenApi userGroup() {
        return GroupedOpenApi.builder()
                .group("User")
                .pathsToMatch("/user/**")
                .build();
    }

    @Bean
    public GroupedOpenApi projectGroup() {
        return GroupedOpenApi.builder()
                .group("Project")
                .pathsToMatch("/projects/**")
                .build();
    }

    @Bean
    public GroupedOpenApi boardGroup() {
        return GroupedOpenApi.builder()
                .group("Board")
                .pathsToMatch("/boards/**")
                .build();
    }

    @Bean
    public GroupedOpenApi applicationGroup() {
        return GroupedOpenApi.builder()
                .group("Application")
                .pathsToMatch("/applicationForms/**")
                .build();
    }

    @Bean
    public GroupedOpenApi notificationGroup() {
        return GroupedOpenApi.builder()
                .group("Notification")
                .pathsToMatch("/notification/**")
                .build();
    }

    @Bean
    public GroupedOpenApi termsGroup() {
        return GroupedOpenApi.builder()
                .group("Terms")
                .pathsToMatch("/terms/**")
                .build();
    }

    @Bean
    public GroupedOpenApi fileGroup() {
        return GroupedOpenApi.builder()
                .group("File")
                .pathsToMatch("/files/**")
                .build();
    }

    @Bean
    public GroupedOpenApi commonGroup() {
        return GroupedOpenApi.builder()
                .group("Common")
                .pathsToMatch("/common/**")
                .build();
    }

    @Bean
    public GroupedOpenApi adminGroup() {
        return GroupedOpenApi.builder()
                .group("Admin")
                .pathsToMatch("/admin/**")
                .build();
    }
}
