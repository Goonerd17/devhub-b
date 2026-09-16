package teamdevhub.devhub.identity.outbound.auth.infrastructure.oauth;

import org.springframework.util.MultiValueMap;
import teamdevhub.devhub.identity.outbound.auth.infrastructure.oauth.http.HeaderProvider;
import teamdevhub.devhub.identity.outbound.auth.infrastructure.oauth.http.HttpResponse;

public interface OAuthHttpClient {

    <T> HttpResponse<T> postFormUrlEncoded(String uri, MultiValueMap<String, String> formData, HeaderProvider headerProvider, Class<T> responseType);
    <T> HttpResponse<T> get(String uri, HeaderProvider headerProvider, Class<T> responseType);
}
