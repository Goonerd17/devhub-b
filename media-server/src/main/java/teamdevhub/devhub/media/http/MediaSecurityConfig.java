package teamdevhub.devhub.media.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import teamdevhub.devhub.shared.internal.InternalApiAuthenticationFilter;
import teamdevhub.devhub.shared.enums.ErrorCode;
import teamdevhub.devhub.web.api.model.response.DataApiResponseDto;

import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.util.Base64;

@Configuration
public class MediaSecurityConfig {
    @Bean
    @Order(1)
    SecurityFilterChain internalApiSecurityFilterChain(HttpSecurity http,
            @Value("${internal.api-key:devhub-local-internal-key}") String apiKey) throws Exception {
        return http.securityMatcher("/internal/**")
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(new InternalApiAuthenticationFilter(apiKey), AnonymousAuthenticationFilter.class)
                .authorizeHttpRequests(auth -> auth.anyRequest().hasRole("INTERNAL_SERVICE"))
                .build();
    }

    @Bean
    @Order(2)
    SecurityFilterChain mediaSecurityFilterChain(HttpSecurity http, ObjectMapper objectMapper) throws Exception {
        return http.csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/files/**").permitAll()
                        .requestMatchers("/actuator/health").permitAll()
                        .anyRequest().authenticated())
                .oauth2ResourceServer(oauth -> oauth.jwt(Customizer.withDefaults())
                        .authenticationEntryPoint((request, response, exception) -> writeError(response, objectMapper,
                                exception instanceof InvalidBearerTokenException ? ErrorCode.TOKEN_INVALID : ErrorCode.AUTH_INVALID)))
                .exceptionHandling(errors -> errors.authenticationEntryPoint(
                        (request, response, exception) -> writeError(response, objectMapper, ErrorCode.AUTH_INVALID)))
                .build();
    }

    @Bean
    JwtDecoder mediaJwtDecoder(@Value("${jwt.secret.key}") String encodedKey) {
        var key = new SecretKeySpec(Base64.getDecoder().decode(encodedKey), "HmacSHA256");
        NimbusJwtDecoder decoder = NimbusJwtDecoder.withSecretKey(key)
                .macAlgorithm(MacAlgorithm.HS256)
                .build();
        decoder.setJwtValidator(new DelegatingOAuth2TokenValidator<>(
                JwtValidators.createDefault(),
                (Jwt jwt) -> "ACCESS".equals(jwt.getClaimAsString("token_type"))
                        ? OAuth2TokenValidatorResult.success()
                        : OAuth2TokenValidatorResult.failure(new OAuth2Error("invalid_token", "Access token required", null))));
        return decoder;
    }

    private void writeError(HttpServletResponse response, ObjectMapper mapper, ErrorCode code) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        mapper.writeValue(response.getWriter(), DataApiResponseDto.failureWithoutData(code));
    }
}
