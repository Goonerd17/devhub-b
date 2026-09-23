package teamdevhub.devhub.query.outbound.home.http;

import java.util.List;
import org.springframework.stereotype.Component;
import teamdevhub.devhub.query.core.port.out.HomeBoardPort;
import teamdevhub.devhub.shared.home.HomeBoardView;

@Component
public class HomeBoardHttpClient implements HomeBoardPort {
    private final HomeBoardFeignClient client;

    public HomeBoardHttpClient(HomeBoardFeignClient client) {
        this.client = client;
    }

    public List<HomeBoardView> findPopular(int limit, boolean sortByLike) {
        return client.findPopular(limit, sortByLike);
    }
}
