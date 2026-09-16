package teamdevhub.devhub.web.api.admin.banner.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import teamdevhub.devhub.administration.core.admin.banner.port.in.command.BannerCommand;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BannerRequsetDto {

    private String publicationStartDate;
    private String publicationEndDate;
    private String alwaysPublication;
    private String used;
    private String bannerType;
    private String bannerGuid;
    private String imageGuid;
    private String description;
    private String title;
    private String link;

    public BannerCommand toCommand() {
        return BannerCommand.builder()
                .publicationStartDate(publicationStartDate)
                .publicationEndDate(publicationEndDate)
                .alwaysPublication(alwaysPublication)
                .used(used)
                .bannerType(bannerType)
                .bannerGuid(bannerGuid)
                .imageGuid(imageGuid)
                .description(description)
                .title(title)
                .link(link)
                .build();
    }

    public BannerCommand toCommand(String bannerGuid) {
        return BannerCommand.builder()
                .publicationStartDate(publicationStartDate)
                .publicationEndDate(publicationEndDate)
                .alwaysPublication(alwaysPublication)
                .used(used)
                .bannerType(bannerType)
                .bannerGuid(bannerGuid)
                .imageGuid(imageGuid)
                .description(description)
                .title(title)
                .link(link)
                .build();
    }
}
