package teamdevhub.devhub.shared.home;

import java.time.LocalDate;

public record HomeBannerView(String bannerGuid, String title, String imageFileGuid, String linkUrl,
        boolean mainBanner, boolean used, LocalDate startDate, LocalDate endDate, int sortOrder) {
}
