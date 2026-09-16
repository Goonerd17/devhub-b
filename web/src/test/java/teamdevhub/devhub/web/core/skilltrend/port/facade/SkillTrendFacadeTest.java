package teamdevhub.devhub.web.core.skilltrend.port.facade;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.web.api.skilltrend.model.response.SkillTrendResponseDto;
import teamdevhub.devhub.readmodel.core.skilltrend.domain.vo.CardInfo;
import teamdevhub.devhub.readmodel.core.skilltrend.domain.vo.SkillCount;
import teamdevhub.devhub.readmodel.core.skilltrend.domain.vo.PositionCount;
import teamdevhub.devhub.readmodel.core.skilltrend.domain.vo.ProjectTimeline;
import teamdevhub.devhub.readmodel.core.skilltrend.domain.vo.SkillSupplyDemand;
import teamdevhub.devhub.web.api.skilltrend.facade.SkillTrendFacade;
import teamdevhub.devhub.fake.pure.application.port.in.usecase.skilltrend.FakeSkillTrendAnalyticsUseCase;
import teamdevhub.devhub.fake.pure.application.port.in.usecase.skilltrend.FakeSkillTrendStatisticsUseCase;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SkillTrendFacadeTest {

    private SkillTrendFacade skillTrendFacade;
    private FakeSkillTrendStatisticsUseCase fakeStatisticsUseCase;
    private FakeSkillTrendAnalyticsUseCase fakeAnalyticsUseCase;

    @BeforeEach
    void init() {
        fakeStatisticsUseCase = new FakeSkillTrendStatisticsUseCase();
        fakeAnalyticsUseCase = new FakeSkillTrendAnalyticsUseCase();
        skillTrendFacade = new SkillTrendFacade(fakeStatisticsUseCase, fakeAnalyticsUseCase);
    }

    @Test
    @DisplayName("스킬_트렌드_조회시_카드정보_포함_모든_통계가_반환된다")
    void getSkillTrendData_allDataPresent_returnsCompleteResponse() {
        // given
        fakeStatisticsUseCase.givenCardInfo(CardInfo.of(100L, 50L, 4.2, 3L));
        fakeStatisticsUseCase.givenDemandedSkills(List.of(
                SkillCount.of("Java", 30L),
                SkillCount.of("Python", 20L)
        ));
        fakeStatisticsUseCase.givenPopularPositions(List.of(
                PositionCount.of("Backend", 40L)
        ));
        fakeStatisticsUseCase.givenProjectTimeline(List.of(
                ProjectTimeline.of(2026, 4, 10L, 5L)
        ));
        fakeAnalyticsUseCase.givenSkillSupplyDemand(List.of(
                SkillSupplyDemand.of("Java", 30L, 25L)
        ));

        // when
        SkillTrendResponseDto result = skillTrendFacade.getSkillTrendData();

        // then
        assertThat(result.cardInfo().projectTotalCnt()).isEqualTo(100L);
        assertThat(result.cardInfo().activeUserCnt()).isEqualTo(50L);
        assertThat(result.demandedSkills()).hasSize(2);
        assertThat(result.popularPositions()).hasSize(1);
        assertThat(result.projectTimeline()).hasSize(1);
        assertThat(result.supplyAndDemandOfSkills()).hasSize(1);
    }

    @Test
    @DisplayName("데이터가_없을_때_빈_통계가_반환된다")
    void getSkillTrendData_noData_returnsEmptyCollections() {
        // given (no setup — fakes return empty/zero values)

        // when
        SkillTrendResponseDto result = skillTrendFacade.getSkillTrendData();

        // then
        assertThat(result.cardInfo().projectTotalCnt()).isZero();
        assertThat(result.demandedSkills()).isEmpty();
        assertThat(result.popularPositions()).isEmpty();
        assertThat(result.projectTimeline()).isEmpty();
        assertThat(result.supplyAndDemandOfSkills()).isEmpty();
        assertThat(result.marketableSkills()).isEmpty();
    }
}
