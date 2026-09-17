package teamdevhub.devhub.query.core.home.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teamdevhub.devhub.query.core.home.domain.Banner;
import teamdevhub.devhub.query.core.home.domain.policy.BannerExposurePolicy;
import teamdevhub.devhub.query.core.home.port.in.query.HomeBoardQuery;
import teamdevhub.devhub.query.core.home.port.in.query.HomeProjectQuery;
import teamdevhub.devhub.query.core.home.port.in.usecase.HomeQueryUseCase;
import teamdevhub.devhub.query.core.home.port.out.LoadHomeBannerPort;
import teamdevhub.devhub.query.core.home.port.out.LoadHomeBoardPort;
import teamdevhub.devhub.query.core.home.port.out.LoadHomeProjectPort;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class HomeQueryService implements HomeQueryUseCase {

    private final LoadHomeBannerPort loadHomeBannerPort;
    private final LoadHomeProjectPort loadHomeProjectPort;
    private final LoadHomeBoardPort loadHomeBoardPort;

    @Override
    public List<Banner> getMainBanners() {
        List<Banner> banners = loadHomeBannerPort.loadMainBanners();
        return BannerExposurePolicy.filterExposable(banners, LocalDate.now());
    }

    @Override
    public List<Banner> getSubBanners() {
        List<Banner> banners = loadHomeBannerPort.loadSubBanners();
        return BannerExposurePolicy.filterExposable(banners, LocalDate.now());
    }

    @Override
    public List<LoadHomeProjectPort.HomeProjectResult> getRecentProjects(HomeProjectQuery query) {
        return loadHomeProjectPort.loadRecentProjects(query);
    }

    @Override
    public List<LoadHomeBoardPort.HomeBoardResult> getPopularBoards(HomeBoardQuery query) {
        return loadHomeBoardPort.loadPopularBoards(query);
    }
}
