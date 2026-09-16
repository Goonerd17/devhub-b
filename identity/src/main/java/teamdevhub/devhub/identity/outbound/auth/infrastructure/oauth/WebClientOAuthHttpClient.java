package teamdevhub.devhub.identity.outbound.auth.infrastructure.oauth;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import teamdevhub.devhub.identity.outbound.auth.infrastructure.oauth.http.HeaderProvider;
import teamdevhub.devhub.identity.outbound.auth.infrastructure.oauth.http.HttpResponse;

@Component
public class WebClientOAuthHttpClient implements OAuthHttpClient {

    private final WebClient oauthWebClient;
    private final ObjectMapper objectMapper;

    public WebClientOAuthHttpClient(@Qualifier("oauthWebClient") WebClient oauthWebClient, ObjectMapper objectMapper) {
        this.oauthWebClient = oauthWebClient;
        this.objectMapper = objectMapper;
    }

    @Override
    public <T> HttpResponse<T> postFormUrlEncoded(String uri, MultiValueMap<String, String> formData, HeaderProvider headerProvider, Class<T> responseType) {
        HttpHeaders httpHeaders = headerProvider.get();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        return oauthWebClient.post()
                .uri(uri)
                .headers(headers -> headers.addAll(httpHeaders))
                .bodyValue(formData)
                .exchangeToMono(response ->
                        response.bodyToMono(String.class)
                                .defaultIfEmpty("")
                                .map(body -> buildResponse(
                                        response.statusCode(),
                                        response.headers().asHttpHeaders(),
                                        body,
                                        responseType
                                ))
                )
                .block();
    }

    @Override
    public <T> HttpResponse<T> get(String uri, HeaderProvider headerProvider, Class<T> responseType) {
        HttpHeaders httpHeaders = headerProvider.get();

        return oauthWebClient.get()
                .uri(uri)
                .headers(headers -> headers.addAll(httpHeaders))
                .exchangeToMono(response ->
                        response.bodyToMono(String.class)
                                .defaultIfEmpty("")
                                .map(body -> buildResponse(
                                        response.statusCode(),
                                        response.headers().asHttpHeaders(),
                                        body,
                                        responseType
                                ))
                )
                .block();
    }

    private <T> HttpResponse<T> buildResponse(
            HttpStatusCode status,
            HttpHeaders headers,
            String rawBody,
            Class<T> responseType
    ) {
        try {
            T body = rawBody.isBlank() ? null : objectMapper.readValue(rawBody, responseType);
            return new HttpResponse<>(status.value(), body, rawBody, headers);

        } catch (Exception e) {
            return new HttpResponse<>(status.value(), null, rawBody, headers);
        }
    }
}