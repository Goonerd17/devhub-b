package teamdevhub.devhub.query.outbound.home.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import teamdevhub.devhub.query.core.home.domain.Banner;
import teamdevhub.devhub.query.core.home.port.out.LoadHomeBannerPort;
import teamdevhub.devhub.shared.home.HomeBannerGateway;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class HomeBannerAdapter implements LoadHomeBannerPort {

    private final HomeBannerGateway homeBannerQueryDao;

    @Override
    public List<Banner> loadMainBanners() {
        return homeBannerQueryDao.findExposable(true, LocalDate.now())
                .stream()
                .map(view -> Banner.of(view.bannerGuid(), view.title(), view.imageFileGuid(), view.linkUrl(),
                        view.mainBanner(), view.used(), view.startDate(), view.endDate(), view.sortOrder()))
                .toList();
    }

    @Override
    public List<Banner> loadSubBanners() {
        return homeBannerQueryDao.findExposable(false, LocalDate.now())
                .stream()
                .map(view -> Banner.of(view.bannerGuid(), view.title(), view.imageFileGuid(), view.linkUrl(),
                        view.mainBanner(), view.used(), view.startDate(), view.endDate(), view.sortOrder()))
                .toList();
    }
}
