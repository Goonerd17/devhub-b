package teamdevhub.devhub.admin.core.admin.banner.port.in.command;

import lombok.Builder;

@Builder
public record BannerCommand(
        String publicationStartDate,
        String publicationEndDate,
        String alwaysPublication,
        String used,
        String bannerType,
        String bannerGuid,
        String imageGuid,
        String description,
        String title,
        String link
) {
}
