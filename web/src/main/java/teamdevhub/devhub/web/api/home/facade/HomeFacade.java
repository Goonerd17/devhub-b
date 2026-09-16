package teamdevhub.devhub.web.api.home.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teamdevhub.devhub.web.api.home.model.response.HomeBannerResponseDto;
import teamdevhub.devhub.web.api.home.model.response.HomeBoardResponseDto;
import teamdevhub.devhub.web.api.home.model.response.HomeProjectResponseDto;
import teamdevhub.devhub.web.api.home.model.response.HomeResponseDto;
import teamdevhub.devhub.readmodel.core.home.port.in.query.HomeBoardQuery;
import teamdevhub.devhub.readmodel.core.home.port.in.query.HomeProjectQuery;
import teamdevhub.devhub.readmodel.core.home.port.in.usecase.HomeQueryUseCase;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class HomeFacade {

    private static final int DEFAULT_PROJECT_LIMIT = 12;
    private static final int DEFAULT_BOARD_LIMIT = 6;

    private final HomeQueryUseCase homeQueryUseCase;

    public HomeResponseDto getHomeData() {
        List<HomeBannerResponseDto> mainBanners = homeQueryUseCase.getMainBanners()
                .stream()
                .map(HomeBannerResponseDto::from)
                .toList();

        List<HomeBannerResponseDto> subBanners = homeQueryUseCase.getSubBanners()
                .stream()
                .map(HomeBannerResponseDto::from)
                .toList();

        List<HomeProjectResponseDto> projects = homeQueryUseCase
                .getRecentProjects(HomeProjectQuery.of(DEFAULT_PROJECT_LIMIT))
                .stream()
                .map(HomeProjectResponseDto::from)
                .toList();

        List<HomeBoardResponseDto> boards = homeQueryUseCase
                .getPopularBoards(HomeBoardQuery.of(DEFAULT_BOARD_LIMIT, HomeBoardQuery.BoardSortType.VIEW_COUNT))
                .stream()
                .map(HomeBoardResponseDto::from)
                .toList();

        return HomeResponseDto.of(mainBanners, projects, subBanners, boards);
    }
}
