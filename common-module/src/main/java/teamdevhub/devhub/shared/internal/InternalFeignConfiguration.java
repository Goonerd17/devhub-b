package teamdevhub.devhub.shared.internal;

import feign.Request;
import feign.RequestInterceptor;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.TimeUnit;

/** Shared transport policy for service-to-service OpenFeign clients. */
public class InternalFeignConfiguration {
    @Bean
    RequestInterceptor internalApiHeaders(
            @Value("${internal.api-key:devhub-local-internal-key}") String apiKey) {
        return template -> {
            template.header(InternalApiAuthenticationFilter.HEADER_NAME, apiKey);
            String correlationId = MDC.get(CorrelationIdFilter.MDC_KEY);
            if (correlationId != null && !correlationId.isBlank()) {
                template.header(CorrelationIdFilter.HEADER_NAME, correlationId);
            }
        };
    }

    @Bean
    Request.Options internalRequestOptions(
            @Value("${internal.http.connect-timeout-ms:3000}") int connectTimeoutMillis,
            @Value("${internal.http.read-timeout-ms:5000}") int readTimeoutMillis) {
        return new Request.Options(connectTimeoutMillis, TimeUnit.MILLISECONDS,
                readTimeoutMillis, TimeUnit.MILLISECONDS, true);
    }
}
