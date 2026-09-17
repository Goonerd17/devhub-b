package teamdevhub.devhub.query.outbound.home.adapter.mapper;

import teamdevhub.devhub.query.core.home.domain.Banner;
import teamdevhub.devhub.query.outbound.home.persistence.BannerReadProjection;

public class BannerMapper {

    private BannerMapper() {}

    public static Banner toDomain(BannerReadProjection entity) {
        return Banner.of(
                entity.bannerGuid(), entity.title(), entity.imageFileGuid(), entity.linkUrl(), entity.mainBanner(),
                entity.used(), entity.startDate(), entity.endDate(), entity.sortOrder()
        );
    }
}
