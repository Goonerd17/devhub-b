package teamdevhub.devhub.readmodel.outbound.home.adapter.mapper;

import teamdevhub.devhub.readmodel.core.home.domain.Banner;
import teamdevhub.devhub.readmodel.outbound.home.persistence.BannerReadProjection;

public class BannerMapper {

    private BannerMapper() {}

    public static Banner toDomain(BannerReadProjection entity) {
        return Banner.of(
                entity.bannerGuid(), entity.title(), entity.imageFileGuid(), entity.linkUrl(), entity.mainBanner(),
                entity.used(), entity.startDate(), entity.endDate(), entity.sortOrder()
        );
    }
}
