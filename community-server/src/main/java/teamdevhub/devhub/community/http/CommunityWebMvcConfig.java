package teamdevhub.devhub.community.http;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import teamdevhub.devhub.community.http.resolver.LoginUserArgumentResolver;
import java.util.List;
@Configuration @RequiredArgsConstructor
public class CommunityWebMvcConfig implements WebMvcConfigurer {
    private final LoginUserArgumentResolver resolver;
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) { resolvers.add(resolver); }
}
