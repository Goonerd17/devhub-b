package teamdevhub.devhub.medium.shared.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import teamdevhub.devhub.web.api.web.resolver.LoginUserArgumentResolver;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = teamdevhub.devhub.DevhubApplication.class)
@AutoConfigureMockMvc
class WebConfigTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private LoginUserArgumentResolver loginUserArgumentResolver;

    @Test
    @DisplayName("LoginUserArgumentResolver_가_등록된다")
    void createLoginUserArgumentResolver() {
        // given
        RequestMappingHandlerAdapter requestMappingHandlerAdapter = context.getBean(RequestMappingHandlerAdapter.class);

        // when
        List<HandlerMethodArgumentResolver> resolvers = requestMappingHandlerAdapter.getArgumentResolvers();

        // then
        assertThat(resolvers).contains(loginUserArgumentResolver);
    }
}
