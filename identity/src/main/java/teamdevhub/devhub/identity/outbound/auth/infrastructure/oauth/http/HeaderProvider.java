package teamdevhub.devhub.identity.outbound.auth.infrastructure.oauth.http;

import org.springframework.http.HttpHeaders;

@FunctionalInterface
public interface HeaderProvider {
    HttpHeaders get();
}
