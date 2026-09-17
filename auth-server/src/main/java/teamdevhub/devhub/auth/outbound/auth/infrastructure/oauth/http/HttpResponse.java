package teamdevhub.devhub.auth.outbound.auth.infrastructure.oauth.http;

import org.springframework.http.HttpHeaders;

public record HttpResponse<T>(int status, T body, String rawBody, HttpHeaders httpHeaders) {

    public boolean is2xx() {
        return status >= 200 && status < 300;
    }
}