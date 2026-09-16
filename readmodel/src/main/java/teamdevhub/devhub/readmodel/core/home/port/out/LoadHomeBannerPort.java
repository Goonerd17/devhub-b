package teamdevhub.devhub.readmodel.core.home.port.out;

import teamdevhub.devhub.readmodel.core.home.domain.Banner;

import java.util.List;

public interface LoadHomeBannerPort {

    List<Banner> loadMainBanners();

    List<Banner> loadSubBanners();
}
