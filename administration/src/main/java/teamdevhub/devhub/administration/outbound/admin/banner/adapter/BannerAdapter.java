package teamdevhub.devhub.administration.outbound.admin.banner.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import teamdevhub.devhub.administration.core.admin.banner.domain.Banner;
import teamdevhub.devhub.administration.core.admin.banner.port.in.command.SearchBannerRequestCommand;
import teamdevhub.devhub.administration.core.admin.banner.port.out.BannerRepository;
import teamdevhub.devhub.platform.core.common.page.PageCommand;
import teamdevhub.devhub.platform.core.common.page.PageResult;
import teamdevhub.devhub.administration.outbound.admin.banner.adapter.mapper.BannerMapper;
import teamdevhub.devhub.administration.outbound.admin.banner.persistence.JpaBannerRepository;
import teamdevhub.devhub.administration.outbound.admin.banner.adapter.entity.BannerEntity;

import java.time.LocalDate;

import static teamdevhub.devhub.platform.shared.enums.DateConstants.MAX_LOCAL_DATE;

@Component
@RequiredArgsConstructor
public class BannerAdapter implements BannerRepository {

    private final JpaBannerRepository jpaBannerRepository;

    @Override
    public PageResult<Banner> getBannerList(SearchBannerRequestCommand searchBannerRequestCommand, PageCommand pageCommand) {
        Pageable pageable = PageRequest.of(pageCommand.page(), pageCommand.size());
        Page<BannerEntity> resultList = jpaBannerRepository.findByComplexCondition(
                searchBannerRequestCommand.keyword(),
                searchBannerRequestCommand.isUsed(),
                searchBannerRequestCommand.isMainBanner(),
                searchBannerRequestCommand.alwaysPublication(),
                searchBannerRequestCommand.publicationStartDate() != null ? LocalDate.parse(searchBannerRequestCommand.publicationStartDate()) : null,
                searchBannerRequestCommand.publicationEndDate() != null ? LocalDate.parse(searchBannerRequestCommand.publicationEndDate()) : null,
                MAX_LOCAL_DATE,
                pageable
        );
        return PageResult.of(
                resultList.getContent().stream().map(BannerMapper::toBanner).toList(),
                resultList.getNumber(),
                resultList.getSize(),
                resultList.getTotalElements()
        );
    }

    @Override
    public void saveBanner(Banner banner) {
        jpaBannerRepository.save(BannerMapper.toEntity(banner));
    }

    @Override
    public void deleteBanner(String bannerGuid) {
        jpaBannerRepository.deleteById(bannerGuid);
    }
}
