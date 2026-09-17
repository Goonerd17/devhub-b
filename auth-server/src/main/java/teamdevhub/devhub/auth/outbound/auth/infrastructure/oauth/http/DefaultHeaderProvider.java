package teamdevhub.devhub.auth.outbound.auth.infrastructure.oauth.http;

import org.springframework.http.HttpHeaders;

public class DefaultHeaderProvider implements HeaderProvider {

    @Override
    public HttpHeaders get() {
        return new HttpHeaders();
    }
}
