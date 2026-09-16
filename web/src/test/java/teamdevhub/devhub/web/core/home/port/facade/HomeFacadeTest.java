package teamdevhub.devhub.web.core.home.port.facade;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.web.api.home.model.response.HomeResponseDto;
import teamdevhub.devhub.readmodel.core.home.domain.Banner;
import teamdevhub.devhub.web.api.home.facade.HomeFacade;
import teamdevhub.devhub.readmodel.core.home.port.out.LoadHomeBoardPort;
import teamdevhub.devhub.readmodel.core.home.port.out.LoadHomeProjectPort;
import teamdevhub.devhub.fake.pure.application.port.in.usecase.home.FakeHomeQueryUseCase;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class HomeFacadeTest {

    private HomeFacade homeFacade;
    private FakeHomeQueryUseCase fakeHomeQueryUseCase;

    @BeforeEach
    void init() {
        fakeHomeQueryUseCase = new FakeHomeQueryUseCase();
        homeFacade = new HomeFacade(fakeHomeQueryUseCase);
    }

    @Test
    @DisplayName("홈_데이터_조회시_배너_프로젝트_게시글이_모두_반환된다")
    void getHomeData_allDataPresent_returnsAggregatedResponse() {
        // given
        Banner mainBanner = Banner.of("b1", "메인배너", null, "http://link1", true, true,
                LocalDate.now().minusDays(1), LocalDate.now().plusDays(30), 1);
        Banner subBanner = Banner.of("b2", "서브배너", null, "http://link2", false, true,
                LocalDate.now().minusDays(1), LocalDate.now().plusDays(30), 1);

        LoadHomeProjectPort.HomeProjectResult project = new LoadHomeProjectPort.HomeProjectResult(
                "p1", "프로젝트1", "개발", "홍길동", null,
                LocalDate.now().toString(), LocalDate.now().plusMonths(1).toString(),
                "RECRUITING", false);

        LoadHomeBoardPort.HomeBoardResult board = new LoadHomeBoardPort.HomeBoardResult(
                "bd1", "게시글1", "001", "김철수", 100, 20L, LocalDate.now().toString());

        fakeHomeQueryUseCase.givenMainBanners(List.of(mainBanner));
        fakeHomeQueryUseCase.givenSubBanners(List.of(subBanner));
        fakeHomeQueryUseCase.givenProjects(List.of(project));
        fakeHomeQueryUseCase.givenBoards(List.of(board));

        // when
        HomeResponseDto result = homeFacade.getHomeData();

        // then
        assertThat(result.mainBannerDataList()).hasSize(1);
        assertThat(result.subBannerDataList()).hasSize(1);
        assertThat(result.projectDataList()).hasSize(1);
        assertThat(result.boardDataList()).hasSize(1);
    }

    @Test
    @DisplayName("데이터가_없을_때_빈_리스트가_반환된다")
    void getHomeData_noData_returnsEmptyLists() {
        // given (no setup — all fakes return empty)

        // when
        HomeResponseDto result = homeFacade.getHomeData();

        // then
        assertThat(result.mainBannerDataList()).isEmpty();
        assertThat(result.subBannerDataList()).isEmpty();
        assertThat(result.projectDataList()).isEmpty();
        assertThat(result.boardDataList()).isEmpty();
    }
}
