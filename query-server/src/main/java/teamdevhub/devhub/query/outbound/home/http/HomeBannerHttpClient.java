package teamdevhub.devhub.query.outbound.home.http;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import teamdevhub.devhub.shared.home.HomeBannerGateway;
import teamdevhub.devhub.shared.home.HomeBannerView;

@Component
public class HomeBannerHttpClient implements HomeBannerGateway {
    private final RestClient client;

    public HomeBannerHttpClient(RestClient.Builder builder,
            @Value("${services.admin.base-url:http://admin-server}") String baseUrl) {
        this.client = builder.baseUrl(baseUrl).build();
    }

    @Override
    public List<HomeBannerView> findExposable(boolean mainBanner, LocalDate today) {
        List<HomeBannerView> result = client.get().uri(uri -> uri.path("/internal/home/banners")
                .queryParam("mainBanner", mainBanner).queryParam("today", today).build())
                .retrieve().body(new ParameterizedTypeReference<>() {});
        return result == null ? List.of() : result;
    }
}
