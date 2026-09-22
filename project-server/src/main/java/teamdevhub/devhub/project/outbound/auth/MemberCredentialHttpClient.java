package teamdevhub.devhub.project.outbound.auth;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import teamdevhub.devhub.shared.security.MemberEmailQuery;

@Component
public class MemberCredentialHttpClient implements MemberEmailQuery {
    private final RestClient client;

    public MemberCredentialHttpClient(RestClient.Builder builder,
            @Value("${services.auth.base-url:http://auth-server}") String baseUrl) {
        this.client = builder.baseUrl(baseUrl).build();
    }

    @Override
    public Optional<String> findEmail(String memberGuid) {
        try {
            return Optional.ofNullable(client.get()
                    .uri("/internal/member-credentials/{memberGuid}/email", memberGuid)
                    .retrieve()
                    .body(String.class));
        } catch (HttpClientErrorException.NotFound ignored) {
            return Optional.empty();
        }
    }
}
