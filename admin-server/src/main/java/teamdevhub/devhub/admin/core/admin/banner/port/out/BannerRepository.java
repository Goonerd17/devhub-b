package teamdevhub.devhub.admin.core.admin.banner.port.out;

import teamdevhub.devhub.admin.core.admin.banner.domain.Banner;
import teamdevhub.devhub.admin.core.admin.banner.port.in.command.SearchBannerRequestCommand;
import teamdevhub.devhub.shared.core.common.page.PageCommand;
import teamdevhub.devhub.shared.core.common.page.PageResult;

public interface BannerRepository {
    PageResult<Banner> getBannerList(SearchBannerRequestCommand searchBannerRequestCommand, PageCommand pageCommand);
    void saveBanner(Banner command);
    void deleteBanner(String bannerGuid);
}
