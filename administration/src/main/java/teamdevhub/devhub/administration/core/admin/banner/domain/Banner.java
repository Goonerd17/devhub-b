package teamdevhub.devhub.administration.core.admin.banner.domain;

import lombok.Builder;
import lombok.Getter;
import teamdevhub.devhub.administration.core.admin.banner.port.in.command.BannerCommand;
import teamdevhub.devhub.platform.core.common.audit.AuditInfo;
import teamdevhub.devhub.platform.shared.enums.DateConstants;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Builder
public class Banner {
    private String bannerGuid;
    private String imageGuid;
    private String publicationStartDate;
    private String publicationEndDate;
    private boolean isUsed;
    private boolean isMainBanner;
    private String description;
    private String title;
    private String link;
    private AuditInfo auditInfo;

    public static Banner ofCommand(BannerCommand command) {
        return Banner.builder()
                .bannerGuid(hasGuid(command.bannerGuid()) ? command.bannerGuid() : getNewGuid())
                .imageGuid(command.imageGuid())
                .publicationStartDate(isBoolean(command.alwaysPublication()) ? LocalDate.now().toString() : command.publicationStartDate())
                .publicationEndDate(isBoolean(command.alwaysPublication()) ? DateConstants.MAX_DATE_STRING : command.publicationEndDate())
                .link(command.link())
                .isUsed(isBoolean(command.used()))
                .isMainBanner("MAIN".equalsIgnoreCase(command.bannerType()))
                .description(command.description())
                .title(command.title())
                .build();
    }

    private static String getNewGuid(){
        return UUID.randomUUID().toString().replace("-", "");
    }

    private static boolean hasGuid(String guid){
        return guid != null && !guid.isEmpty();
    }

    private static boolean isBoolean(String value){
        return "Y".equalsIgnoreCase(value);
    }
}
