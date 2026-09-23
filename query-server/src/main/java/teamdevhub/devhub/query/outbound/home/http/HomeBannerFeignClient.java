package teamdevhub.devhub.query.outbound.home.http;

import java.time.LocalDate;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import teamdevhub.devhub.query.core.port.out.HomeBannerPort;
import teamdevhub.devhub.shared.home.HomeBannerView;
import teamdevhub.devhub.shared.internal.InternalFeignConfiguration;

@FeignClient(name = "admin-server", configuration = InternalFeignConfiguration.class)
public interface HomeBannerFeignClient extends HomeBannerPort {
    @Override
    @GetMapping("/internal/home/banners")
    List<HomeBannerView> findExposable(@RequestParam boolean mainBanner, @RequestParam LocalDate today);
}
