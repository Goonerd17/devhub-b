package teamdevhub.devhub.query.outbound.home.http;

import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import teamdevhub.devhub.shared.home.HomeBoardGateway;
import teamdevhub.devhub.shared.home.HomeBoardView;

@Component
public class HomeBoardHttpClient implements HomeBoardGateway {
    private final RestClient client;

    public HomeBoardHttpClient(RestClient.Builder builder,
            @Value("${services.community.base-url:http://community-server}") String baseUrl) {
        this.client = builder.baseUrl(baseUrl).build();
    }

    public List<HomeBoardView> findPopular(int limit, boolean sortByLike) {
        List<HomeBoardView> result = client.get().uri(uri -> uri.path("/internal/home/boards")
                .queryParam("limit", limit).queryParam("sortByLike", sortByLike).build())
                .retrieve().body(new ParameterizedTypeReference<>() {});
        return result == null ? List.of() : result;
    }
}
