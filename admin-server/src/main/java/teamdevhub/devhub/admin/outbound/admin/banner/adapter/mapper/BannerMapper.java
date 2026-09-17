package teamdevhub.devhub.admin.outbound.admin.banner.adapter.mapper;

import teamdevhub.devhub.admin.core.admin.banner.domain.Banner;
import teamdevhub.devhub.shared.core.common.audit.AuditInfo;
import teamdevhub.devhub.admin.outbound.admin.banner.adapter.entity.BannerEntity;

import java.time.LocalDate;

public class BannerMapper {

    private BannerMapper() {}

    private static AuditInfo toAuditInfo(BannerEntity entity) {
        return AuditInfo.of(
                entity.getRegistrantGuid(),
                entity.getRegisteredDate(),
                entity.getModifierGuid(),
                entity.getModifiedDate()
        );
    }

    public static Banner toBanner(BannerEntity bannerEntity) {
        return Banner.builder()
                .bannerGuid(bannerEntity.getBannerGuid())
                .publicationStartDate(bannerEntity.getStartDate().toString())
                .publicationEndDate(bannerEntity.getEndDate().toString())
                .imageGuid(bannerEntity.getImageFileGuid())
                .title(bannerEntity.getTitle())
                .description(bannerEntity.getDescription())
                .isUsed(bannerEntity.isUsed())
                .isMainBanner(bannerEntity.isMainBanner())
                .link(bannerEntity.getLinkUrl())
                .auditInfo(toAuditInfo(bannerEntity))
                .build();
    }

    public static BannerEntity toEntity(Banner banner) {
        return BannerEntity.builder()
                .bannerGuid(banner.getBannerGuid())
                .linkUrl(banner.getLink())
                .title(banner.getTitle())
                .startDate(LocalDate.parse(banner.getPublicationStartDate()))
                .endDate(LocalDate.parse(banner.getPublicationEndDate()))
                .description(banner.getDescription())
                .mainBanner(banner.isMainBanner())
                .used(banner.isUsed())
                .imageFileGuid(banner.getImageGuid())
                .sortOrder(0)
                .build();
    }
}
