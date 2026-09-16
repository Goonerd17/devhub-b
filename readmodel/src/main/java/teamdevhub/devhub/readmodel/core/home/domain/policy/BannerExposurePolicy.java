package teamdevhub.devhub.readmodel.core.home.domain.policy;

import teamdevhub.devhub.readmodel.core.home.domain.Banner;

import java.time.LocalDate;
import java.util.List;

public class BannerExposurePolicy {

    private BannerExposurePolicy() {}

    public static List<Banner> filterExposable(List<Banner> banners, LocalDate today) {
        return banners.stream()
                .filter(banner -> banner.isExposable(today))
                .toList();
    }
}
