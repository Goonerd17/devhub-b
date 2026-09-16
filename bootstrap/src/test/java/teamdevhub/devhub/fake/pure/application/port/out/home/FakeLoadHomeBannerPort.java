package teamdevhub.devhub.fake.pure.application.port.out.home;

import teamdevhub.devhub.readmodel.core.home.domain.Banner;
import teamdevhub.devhub.readmodel.core.home.port.out.LoadHomeBannerPort;

import java.util.ArrayList;
import java.util.List;

public class FakeLoadHomeBannerPort implements LoadHomeBannerPort {

    private final List<Banner> mainBanners = new ArrayList<>();
    private final List<Banner> subBanners = new ArrayList<>();

    public void givenMainBanners(List<Banner> banners) {
        mainBanners.clear();
        mainBanners.addAll(banners);
    }

    public void givenSubBanners(List<Banner> banners) {
        subBanners.clear();
        subBanners.addAll(banners);
    }

    @Override
    public List<Banner> loadMainBanners() {
        return List.copyOf(mainBanners);
    }

    @Override
    public List<Banner> loadSubBanners() {
        return List.copyOf(subBanners);
    }
}
