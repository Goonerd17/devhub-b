package teamdevhub.devhub.readmodel.outbound.home.persistence;

import java.time.LocalDate;

/** Readmodel 전용 배너 조회 결과. 외부 Administration Entity를 노출하지 않는다. */
public record BannerReadProjection(String bannerGuid, String title, String imageFileGuid, String linkUrl,
                                   boolean mainBanner, boolean used, LocalDate startDate, LocalDate endDate,
                                   int sortOrder) {
}
