package teamdevhub.devhub.query.core.home.port.out;

import teamdevhub.devhub.query.core.home.domain.Banner;

import java.util.List;

public interface LoadHomeBannerPort {

    List<Banner> loadMainBanners();

    List<Banner> loadSubBanners();
}
