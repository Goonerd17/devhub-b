package teamdevhub.devhub.query.http.home.model.response;

import java.util.List;

public record HomeResponseDto(
        List<HomeBannerResponseDto> mainBannerDataList,
        List<HomeProjectResponseDto> projectDataList,
        List<HomeBannerResponseDto> subBannerDataList,
        List<HomeBoardResponseDto> boardDataList
) {

    public static HomeResponseDto of(
            List<HomeBannerResponseDto> mainBanners,
            List<HomeProjectResponseDto> projects,
            List<HomeBannerResponseDto> subBanners,
            List<HomeBoardResponseDto> boards
    ) {
        return new HomeResponseDto(mainBanners, projects, subBanners, boards);
    }
}

