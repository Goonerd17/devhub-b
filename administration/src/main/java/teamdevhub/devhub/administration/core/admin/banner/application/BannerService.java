package teamdevhub.devhub.administration.core.admin.banner.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teamdevhub.devhub.administration.core.admin.banner.domain.Banner;
import teamdevhub.devhub.administration.core.admin.banner.port.in.command.BannerCommand;
import teamdevhub.devhub.administration.core.admin.banner.port.in.command.SearchBannerRequestCommand;
import teamdevhub.devhub.administration.core.admin.banner.port.in.usecase.BannerUseCase;
import teamdevhub.devhub.administration.core.admin.banner.port.out.BannerRepository;
import teamdevhub.devhub.platform.core.common.page.PageCommand;
import teamdevhub.devhub.platform.core.common.page.PageResult;

@Service
@Transactional
@RequiredArgsConstructor
public class BannerService implements BannerUseCase {

    private final BannerRepository bannerRepository;

    @Override
    public PageResult<Banner> getBannerList(SearchBannerRequestCommand searchBannerRequestCommand, PageCommand pageCommand) {
        return bannerRepository.getBannerList(searchBannerRequestCommand, pageCommand);
    }

    @Override
    public void saveBanner(BannerCommand command) {
        bannerRepository.saveBanner(Banner.ofCommand(command));
    }

    @Override
    public void deleteBanner(String bannerGuid) {
        bannerRepository.deleteBanner(bannerGuid);
    }

}
