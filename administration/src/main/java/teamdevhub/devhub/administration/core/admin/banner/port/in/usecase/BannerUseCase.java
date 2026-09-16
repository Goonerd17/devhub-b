package teamdevhub.devhub.administration.core.admin.banner.port.in.usecase;

import teamdevhub.devhub.administration.core.admin.banner.domain.Banner;
import teamdevhub.devhub.administration.core.admin.banner.port.in.command.BannerCommand;
import teamdevhub.devhub.administration.core.admin.banner.port.in.command.SearchBannerRequestCommand;
import teamdevhub.devhub.platform.core.common.page.PageCommand;
import teamdevhub.devhub.platform.core.common.page.PageResult;

public interface BannerUseCase {
    PageResult<Banner> getBannerList(SearchBannerRequestCommand searchBannerRequestCommand, PageCommand pageCommand);
    void saveBanner(BannerCommand command);
    void deleteBanner(String bannerGuid);
}
