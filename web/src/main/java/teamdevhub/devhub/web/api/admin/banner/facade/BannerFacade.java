package teamdevhub.devhub.web.api.admin.banner.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import teamdevhub.devhub.web.api.admin.banner.model.response.BannerResponseDto;
import teamdevhub.devhub.web.api.web.model.response.DataApiResponseDto;
import teamdevhub.devhub.web.api.web.model.response.DataListApiResponseDto;
import teamdevhub.devhub.web.api.web.model.response.PageResponseDto;
import teamdevhub.devhub.administration.core.admin.banner.domain.Banner;
import teamdevhub.devhub.administration.core.admin.banner.port.in.command.BannerCommand;
import teamdevhub.devhub.administration.core.admin.banner.port.in.command.SearchBannerRequestCommand;
import teamdevhub.devhub.administration.core.admin.banner.port.in.usecase.BannerUseCase;
import teamdevhub.devhub.platform.core.common.page.PageCommand;
import teamdevhub.devhub.platform.core.common.page.PageResult;
import teamdevhub.devhub.web.shared.enums.SuccessCode;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BannerFacade {

    private final BannerUseCase bannerUseCase;

    public DataListApiResponseDto<BannerResponseDto> getBannerList(SearchBannerRequestCommand searchBannerRequestCommand, PageCommand pageCommand) {
        PageResult<Banner> pageDataList = bannerUseCase.getBannerList(searchBannerRequestCommand, pageCommand);
        List<BannerResponseDto> returnData = pageDataList.content().stream()
                .map(BannerResponseDto::fromDomain)
                .toList();

        return DataListApiResponseDto.successWithDataList(
                SuccessCode.READ_SUCCESS,
                returnData,
                PageResponseDto.from(pageDataList)
        );
    }

    public DataApiResponseDto<?> saveBanner(BannerCommand command) {
        bannerUseCase.saveBanner(command);
        return DataApiResponseDto.successWithData(
                SuccessCode.READ_SUCCESS,
                null
        );
    }

    public DataApiResponseDto<?> deleteBanner(String bannerGuid) {
        bannerUseCase.deleteBanner(bannerGuid);
        return DataApiResponseDto.successWithData(
                SuccessCode.READ_SUCCESS,
                null
        );
    }
}
