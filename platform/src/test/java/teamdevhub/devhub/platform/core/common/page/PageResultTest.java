package teamdevhub.devhub.platform.core.common.page;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.platform.core.common.page.PageResult;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PageResultTest {

    @Test
    @DisplayName("페이지_결과가_정상적으로_생성된다")
    void createPageResultCorrectly() {
        // given
        List<String> content = List.of("A", "B", "C");
        int page = 1;
        int size = 3;
        long totalElements = 10;

        // when
        PageResult<String> result = PageResult.of(content, page, size, totalElements);

        // then
        assertThat(result.content()).containsExactly("A", "B", "C");
        assertThat(result.page()).isEqualTo(1);
        assertThat(result.size()).isEqualTo(3);
        assertThat(result.totalElements()).isEqualTo(10);
        assertThat(result.totalPages()).isEqualTo(4);
        assertThat(result.first()).isFalse();
        assertThat(result.last()).isFalse();
    }

    @Test
    @DisplayName("첫_페이지이면_first_는_true_이다")
    void firstPageShouldBeTrue() {
        // given
        int page = 0;
        int size = 5;
        long totalElements = 20;

        // when
        PageResult<String> result = PageResult.of(List.of(), page, size, totalElements);

        // then
        assertThat(result.first()).isTrue();
        assertThat(result.last()).isFalse();
    }

    @Test
    @DisplayName("마지막_페이지이면_last_는_true_이다")
    void lastPageShouldBeTrue() {
        // given
        int page = 3;
        int size = 5;
        long totalElements = 20;

        // when
        PageResult<String> result = PageResult.of(List.of(), page, size, totalElements);

        // then
        assertThat(result.first()).isFalse();
        assertThat(result.last()).isTrue();
    }

    @Test
    @DisplayName("전체가_한_페이지뿐이면_first_와_last_는_모두_true_이다")
    void singlePageShouldBeFirstAndLast() {
        // given
        int page = 0;
        int size = 10;
        long totalElements = 5;

        // when
        PageResult<String> result = PageResult.of(List.of("A"), page, size, totalElements);

        // then
        assertThat(result.totalPages()).isEqualTo(1);
        assertThat(result.first()).isTrue();
        assertThat(result.last()).isTrue();
    }

    @Test
    @DisplayName("totalElements_가_0이면_totalPages_는_0이다")
    void zeroTotalElementsResultsInZeroPages() {
        // given
        int page = 0;
        int size = 10;
        long totalElements = 0;

        // when
        PageResult<String> result = PageResult.of(List.of(), page, size, totalElements);

        // then
        assertThat(result.totalPages()).isEqualTo(0);
        assertThat(result.first()).isTrue();
        assertThat(result.last()).isTrue();
    }

    @Test
    @DisplayName("size_가_0_이하면_예외가_발생한다")
    void sizeLessThanOrEqualZeroThrowsException() {
        // given
        int size = 0;

        // when, then
        assertThatThrownBy(() ->
                PageResult.of(List.of(), 0, size, 10)
        )
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("size must be greater than 0");
    }
}
