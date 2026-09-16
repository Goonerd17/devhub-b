package teamdevhub.devhub.identity.outbound.auth.infrastructure.oauth.http;

import org.springframework.http.HttpHeaders;

public class BearerAuthHeaderProvider implements HeaderProvider {

    private final String accessToken;

    public BearerAuthHeaderProvider(String accessToken) {
        this.accessToken = accessToken;
    }

    @Override
    public HttpHeaders get() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(accessToken);
        return httpHeaders;
    }
}