package teamdevhub.devhub.web.api.home.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import teamdevhub.devhub.web.api.home.controller.HomeController;
import teamdevhub.devhub.web.api.home.model.response.HomeBannerResponseDto;
import teamdevhub.devhub.web.api.home.model.response.HomeBoardResponseDto;
import teamdevhub.devhub.web.api.home.model.response.HomeProjectResponseDto;
import teamdevhub.devhub.web.api.home.model.response.HomeResponseDto;
import teamdevhub.devhub.web.api.web.model.response.DataApiResponseDto;
import teamdevhub.devhub.web.api.home.facade.HomeFacade;
import teamdevhub.devhub.web.shared.enums.SuccessCode;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class HomeControllerTest {

    private HomeController homeController;
    private HomeFacade homeFacade;

    @BeforeEach
    void init() {
        homeFacade = Mockito.mock(HomeFacade.class);
        homeController = new HomeController(homeFacade);
    }

    @Test
    @DisplayName("홈_데이터_조회시_READ_SUCCESS_코드와_홈_데이터가_반환된다")
    void getHomeData_success_returnsReadSuccessWithData() {
        // given
        HomeResponseDto homeResponseDto = HomeResponseDto.of(
                List.of(new HomeBannerResponseDto("banner-1", "메인배너", "file-1", "https://example.com", 1)),
                List.of(new HomeProjectResponseDto("project-1", "프로젝트 제목", "WEB", "user1", null, null, null, "RECRUITING")),
                List.of(new HomeBannerResponseDto("banner-2", "서브배너", "file-2", "https://sub.com", 1)),
                List.of(new HomeBoardResponseDto("board-1", "게시글 제목", "FREE", "user1", 100, 50L, "2026-05-01"))
        );
        when(homeFacade.getHomeData()).thenReturn(homeResponseDto);

        // when
        ResponseEntity<DataApiResponseDto<HomeResponseDto>> response = homeController.getHomeData();

        // then
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCode()).isEqualTo(SuccessCode.READ_SUCCESS.getCode());

        HomeResponseDto data = response.getBody().getData();
        assertThat(data.mainBannerDataList()).hasSize(1);
        assertThat(data.projectDataList()).hasSize(1);
        assertThat(data.subBannerDataList()).hasSize(1);
        assertThat(data.boardDataList()).hasSize(1);

        verify(homeFacade).getHomeData();
    }

    @Test
    @DisplayName("홈_데이터가_없을_때_빈_리스트로_응답한다")
    void getHomeData_emptyData_returnsEmptyLists() {
        // given
        HomeResponseDto emptyResponse = HomeResponseDto.of(
                List.of(), List.of(), List.of(), List.of()
        );
        when(homeFacade.getHomeData()).thenReturn(emptyResponse);

        // when
        ResponseEntity<DataApiResponseDto<HomeResponseDto>> response = homeController.getHomeData();

        // then
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCode()).isEqualTo(SuccessCode.READ_SUCCESS.getCode());

        HomeResponseDto data = response.getBody().getData();
        assertThat(data.mainBannerDataList()).isEmpty();
        assertThat(data.projectDataList()).isEmpty();
        assertThat(data.subBannerDataList()).isEmpty();
        assertThat(data.boardDataList()).isEmpty();

        verify(homeFacade).getHomeData();
    }
}
