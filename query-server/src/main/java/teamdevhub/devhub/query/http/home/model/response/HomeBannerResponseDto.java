package teamdevhub.devhub.query.http.home.model.response;

import teamdevhub.devhub.query.core.home.domain.Banner;

public record HomeBannerResponseDto(
        String bannerGuid,
        String title,
        String imageFileGuid,
        String linkUrl,
        int sortOrder
) {

    public static HomeBannerResponseDto from(Banner banner) {
        return new HomeBannerResponseDto(
                banner.getBannerGuid(),
                banner.getTitle(),
                banner.getImageFileGuid(),
                banner.getLinkUrl(),
                banner.getSortOrder()
        );
    }
}

