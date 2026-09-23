package teamdevhub.devhub.project.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import teamdevhub.devhub.shared.internal.InternalApiAuthenticationFilter;

@Configuration
public class InternalApiSecurityConfig {
    @Bean @Order(1)
    SecurityFilterChain internalApiSecurity(HttpSecurity http,
            @org.springframework.beans.factory.annotation.Value("${internal.api-key:devhub-local-internal-key}") String apiKey)
            throws Exception {
        return http.securityMatcher("/internal/**").csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(new InternalApiAuthenticationFilter(apiKey), AnonymousAuthenticationFilter.class)
                .authorizeHttpRequests(auth -> auth.anyRequest().hasRole("INTERNAL_SERVICE")).build();
    }

    @Bean @Order(2)
    SecurityFilterChain defaultSecurity(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
                .httpBasic(Customizer.withDefaults()).build();
    }
}
