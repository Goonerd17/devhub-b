package teamdevhub.devhub.query.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import teamdevhub.devhub.shared.internal.InternalApiAuthenticationFilter;

@Configuration
public class InternalRestClientConfig {
    @Bean
    @LoadBalanced
    RestClient.Builder internalRestClientBuilder(
            @org.springframework.beans.factory.annotation.Value("${internal.api-key:devhub-local-internal-key}") String apiKey) {
        return RestClient.builder().defaultHeader(InternalApiAuthenticationFilter.HEADER_NAME, apiKey);
    }
}
