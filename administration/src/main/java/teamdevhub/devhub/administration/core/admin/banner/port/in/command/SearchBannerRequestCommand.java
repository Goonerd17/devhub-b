package teamdevhub.devhub.administration.core.admin.banner.port.in.command;

import lombok.Builder;

@Builder
public record SearchBannerRequestCommand(
        String publicationStartDate,
        String publicationEndDate,
        Boolean alwaysPublication,
        Boolean isUsed,
        String keyword,
        Boolean isMainBanner
) {
}
