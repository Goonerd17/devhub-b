package teamdevhub.devhub.readmodel.core.home.domain;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class Banner {

    private final String bannerGuid;
    private final String title;
    private final String imageFileGuid;
    private final String linkUrl;
    private final boolean mainBanner;
    private final boolean used;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final int sortOrder;

    @Builder
    private Banner(
            String bannerGuid,
            String title,
            String imageFileGuid,
            String linkUrl,
            boolean mainBanner,
            boolean used,
            LocalDate startDate,
            LocalDate endDate,
            int sortOrder
    ) {
        this.bannerGuid = bannerGuid;
        this.title = title;
        this.imageFileGuid = imageFileGuid;
        this.linkUrl = linkUrl;
        this.mainBanner = mainBanner;
        this.used = used;
        this.startDate = startDate;
        this.endDate = endDate;
        this.sortOrder = sortOrder;
    }

    public static Banner of(
            String bannerGuid,
            String title,
            String imageFileGuid,
            String linkUrl,
            boolean mainBanner,
            boolean used,
            LocalDate startDate,
            LocalDate endDate,
            int sortOrder
    ) {
        return Banner.builder()
                .bannerGuid(bannerGuid)
                .title(title)
                .imageFileGuid(imageFileGuid)
                .linkUrl(linkUrl)
                .mainBanner(mainBanner)
                .used(used)
                .startDate(startDate)
                .endDate(endDate)
                .sortOrder(sortOrder)
                .build();
    }

    public boolean isExposable(LocalDate today) {
        return used
                && startDate != null && endDate != null
                && !today.isBefore(startDate)
                && !today.isAfter(endDate);
    }
}
