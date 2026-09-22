package teamdevhub.devhub.query.outbound.home.http;

import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import teamdevhub.devhub.shared.home.HomeProjectGateway;
import teamdevhub.devhub.shared.home.HomeProjectView;

@Component
public class HomeProjectHttpClient implements HomeProjectGateway {
    private final RestClient client;

    public HomeProjectHttpClient(RestClient.Builder builder,
            @Value("${services.project.base-url:http://project-server}") String baseUrl) {
        this.client = builder.baseUrl(baseUrl).build();
    }

    public List<HomeProjectView> findRecent(LocalDate today, int limit) {
        List<HomeProjectView> result = client.get().uri(uri -> uri.path("/internal/home/projects")
                .queryParam("today", today).queryParam("limit", limit).build())
                .retrieve().body(new ParameterizedTypeReference<>() {});
        return result == null ? List.of() : result;
    }
}
