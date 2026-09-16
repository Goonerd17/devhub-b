package teamdevhub.devhub.web.api.admin.banner.model.request;

import lombok.*;
import teamdevhub.devhub.administration.core.admin.banner.port.in.command.SearchBannerRequestCommand;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SearchBannerRequestDto {
    private String publicationStartDate;
    private String publicationEndDate;
    private String alwaysPublication;
    private String used;
    private String keyword;
    private String bannerType;

    public SearchBannerRequestCommand toCommand() {
        return SearchBannerRequestCommand.builder()
                .publicationStartDate(publicationStartDate)
                .publicationEndDate(publicationEndDate)
                .isUsed("Y".equalsIgnoreCase(used) ? Boolean.TRUE : "N".equalsIgnoreCase(used) ? Boolean.FALSE : null)
                .alwaysPublication("Y".equalsIgnoreCase(alwaysPublication) ? Boolean.TRUE : "N".equalsIgnoreCase(alwaysPublication) ? Boolean.FALSE : null)
                .keyword(keyword)
                .isMainBanner("MAIN".equalsIgnoreCase(bannerType))
                .build();
    }
}
