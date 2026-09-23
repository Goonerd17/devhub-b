package teamdevhub.devhub.query.outbound.home.http;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Component;
import teamdevhub.devhub.query.core.port.out.HomeBannerPort;
import teamdevhub.devhub.shared.home.HomeBannerView;

@Component
public class HomeBannerHttpClient implements HomeBannerPort {
    private final HomeBannerFeignClient client;

    public HomeBannerHttpClient(HomeBannerFeignClient client) {
        this.client = client;
    }

    @Override
    public List<HomeBannerView> findExposable(boolean mainBanner, LocalDate today) {
        return client.findExposable(mainBanner, today);
    }
}
