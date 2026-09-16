package teamdevhub.devhub.web.api.web.model.response;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.platform.core.common.page.PageResult;
import teamdevhub.devhub.web.api.web.model.response.PageResponseDto;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PageResponseDtoTest {

    @Test
    @DisplayName("PageResult_로부터_PageVo_를_생성한다")
    void createPageVoFromPageResult() {
        // given
        PageResult<String> pageResult = PageResult.of(
                List.of("data1", "data2"),
                1,
                10,
                25
        );

        // when
        PageResponseDto pageResponseDto = PageResponseDto.from(pageResult);

        // then
        assertThat(pageResponseDto.getPage()).isEqualTo(1);
        assertThat(pageResponseDto.getSize()).isEqualTo(10);
        assertThat(pageResponseDto.getTotalPages()).isEqualTo(3);
        assertThat(pageResponseDto.getTotalElements()).isEqualTo(25);
        assertThat(pageResponseDto.isFirst()).isFalse();
        assertThat(pageResponseDto.isLast()).isFalse();
    }

    @Test
    @DisplayName("첫_페이지일_경우_first_는_true_이고_last_는_false_이다")
    void setFirstTrueAndLastFalseForFirstPage() {
        // given
        PageResult<String> pageResult = PageResult.of(
                List.of("data"),
                0,
                10,
                25
        );

        // when
        PageResponseDto pageResponseDto = PageResponseDto.from(pageResult);

        // then
        assertThat(pageResponseDto.isFirst()).isTrue();
        assertThat(pageResponseDto.isLast()).isFalse();
    }

    @Test
    @DisplayName("마지막_페이지일_경우_last_는_true_이다")
    void setLastTrueForLastPage() {
        // given
        PageResult<String> pageResult = PageResult.of(
                List.of("data"),
                2,
                10,
                25
        );

        // when
        PageResponseDto pageResponseDto = PageResponseDto.from(pageResult);

        // then
        assertThat(pageResponseDto.isLast()).isTrue();
    }
}
