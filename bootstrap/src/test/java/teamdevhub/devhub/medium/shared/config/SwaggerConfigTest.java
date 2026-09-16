package teamdevhub.devhub.medium.shared.config;

import io.swagger.v3.oas.models.OpenAPI;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = teamdevhub.devhub.DevhubApplication.class)
class SwaggerConfigTest {

    @Autowired
    private OpenAPI openAPI;

    @Autowired
    private GroupedOpenApi userGroup;

    @Test
    @DisplayName("openAPI_빈이_등록된다")
    void createOpenAPIBean() {
        // given, when, then
        assertThat(openAPI).isNotNull();
        assertThat(openAPI.getInfo().getTitle()).isEqualTo("DevHub API");
    }

    @Test
    @DisplayName("userGroup_빈이_등록된다")
    void createUserGroupBean() {
        // given, when, then
        assertThat(userGroup).isNotNull();
        assertThat(userGroup.getGroup()).isEqualTo("User");
    }
}
