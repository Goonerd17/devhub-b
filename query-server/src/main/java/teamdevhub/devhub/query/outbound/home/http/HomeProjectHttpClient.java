package teamdevhub.devhub.query.outbound.home.http;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Component;
import teamdevhub.devhub.query.core.port.out.HomeProjectPort;
import teamdevhub.devhub.shared.home.HomeProjectView;

@Component
public class HomeProjectHttpClient implements HomeProjectPort {
    private final HomeProjectFeignClient client;

    public HomeProjectHttpClient(HomeProjectFeignClient client) {
        this.client = client;
    }

    public List<HomeProjectView> findRecent(LocalDate today, int limit) {
        return client.findRecent(today, limit);
    }
}
