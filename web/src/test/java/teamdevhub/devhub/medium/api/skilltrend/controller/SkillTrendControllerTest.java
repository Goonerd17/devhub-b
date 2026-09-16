package teamdevhub.devhub.web.api.skilltrend.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import teamdevhub.devhub.web.api.skilltrend.controller.SkillTrendController;
import teamdevhub.devhub.web.api.skilltrend.model.response.CardInfoResponseDto;
import teamdevhub.devhub.web.api.skilltrend.model.response.SkillTrendResponseDto;
import teamdevhub.devhub.web.api.web.model.response.DataApiResponseDto;
import teamdevhub.devhub.web.api.skilltrend.facade.SkillTrendFacade;
import teamdevhub.devhub.web.shared.enums.SuccessCode;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class SkillTrendControllerTest {

    private SkillTrendController skillTrendController;
    private SkillTrendFacade skillTrendFacade;

    @BeforeEach
    void init() {
        skillTrendFacade = Mockito.mock(SkillTrendFacade.class);
        skillTrendController = new SkillTrendController(skillTrendFacade);
    }

    @Test
    @DisplayName("스킬_트렌드_조회시_READ_SUCCESS_코드와_통계_데이터가_반환된다")
    void getSkillTrendData_success_returnsReadSuccessWithData() {
        // given
        SkillTrendResponseDto skillTrendResponseDto = SkillTrendResponseDto.of(
                new CardInfoResponseDto(120L, 80L, 4.1, 5L),
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                List.of()
        );
        when(skillTrendFacade.getSkillTrendData()).thenReturn(skillTrendResponseDto);

        // when
        ResponseEntity<DataApiResponseDto<SkillTrendResponseDto>> response = skillTrendController.getSkillTrendData();

        // then
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCode()).isEqualTo(SuccessCode.READ_SUCCESS.getCode());

        SkillTrendResponseDto data = response.getBody().getData();
        assertThat(data.cardInfo().projectTotalCnt()).isEqualTo(120L);
        assertThat(data.cardInfo().activeUserCnt()).isEqualTo(80L);

        verify(skillTrendFacade).getSkillTrendData();
    }

    @Test
    @DisplayName("스킬_트렌드_데이터가_없을_때_빈_컬렉션으로_응답한다")
    void getSkillTrendData_emptyData_returnsEmptyCollections() {
        // given
        SkillTrendResponseDto emptyResponse = SkillTrendResponseDto.of(
                new CardInfoResponseDto(0L, 0L, 0.0, 0L),
                List.of(), List.of(), List.of(), List.of(), List.of(), List.of()
        );
        when(skillTrendFacade.getSkillTrendData()).thenReturn(emptyResponse);

        // when
        ResponseEntity<DataApiResponseDto<SkillTrendResponseDto>> response = skillTrendController.getSkillTrendData();

        // then
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCode()).isEqualTo(SuccessCode.READ_SUCCESS.getCode());

        SkillTrendResponseDto data = response.getBody().getData();
        assertThat(data.cardInfo().projectTotalCnt()).isZero();
        assertThat(data.demandedSkills()).isEmpty();
        assertThat(data.popularPositions()).isEmpty();
        assertThat(data.projectTimeline()).isEmpty();
        assertThat(data.supplyAndDemandOfSkills()).isEmpty();
        assertThat(data.marketableSkills()).isEmpty();

        verify(skillTrendFacade).getSkillTrendData();
    }
}
