package teamdevhub.devhub.admin.http.admin.banner.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import teamdevhub.devhub.admin.core.admin.banner.domain.Banner;
import teamdevhub.devhub.shared.enums.DateConstants;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BannerResponseDto {

    private String bannerGuid;
    private String imageGuid;
    private String publicationStartDate;
    private String publicationEndDate;
    private String alwaysPublication;
    private String used;
    private String bannerType;
    private String description;
    private String title;
    private String link;

    public static BannerResponseDto fromDomain(Banner banner) {
        return BannerResponseDto.builder()
                .bannerGuid(banner.getBannerGuid())
                .imageGuid(banner.getImageGuid())
                .publicationStartDate(banner.getPublicationStartDate())
                .publicationEndDate(banner.getPublicationEndDate())
                .bannerType(banner.isMainBanner() ? "MAIN" : "SUB")
                .description(banner.getDescription())
                .title(banner.getTitle())
                .link(banner.getLink())
                .used(banner.isUsed() ? "Y" : "N")
                .alwaysPublication(DateConstants.MAX_DATE_STRING.equalsIgnoreCase(banner.getPublicationEndDate()) ? "Y" : "N")
                .build();
    }
}
