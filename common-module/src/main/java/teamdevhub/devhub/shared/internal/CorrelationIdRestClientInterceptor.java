package teamdevhub.devhub.shared.internal;

import org.slf4j.MDC;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

/** Propagates the inbound correlation id to synchronous service-to-service calls. */
public final class CorrelationIdRestClientInterceptor implements ClientHttpRequestInterceptor {
    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body,
            ClientHttpRequestExecution execution) throws IOException {
        String correlationId = MDC.get(CorrelationIdFilter.MDC_KEY);
        if (correlationId != null && !correlationId.isBlank()
                && !request.getHeaders().containsKey(CorrelationIdFilter.HEADER_NAME)) {
            request.getHeaders().set(CorrelationIdFilter.HEADER_NAME, correlationId);
        }
        return execution.execute(request, body);
    }
}
