package teamdevhub.devhub.platform.core.common.page;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.platform.core.common.page.PageCommand;

import static org.assertj.core.api.Assertions.assertThat;

class PageCommandTest {

    @Test
    @DisplayName("정상적인_page_와_size_로_PageCommand_를_생성한다")
    void createCommandWithValidPageAndSize() {
        // given
        PageCommand pageCommand = PageCommand.of(2, 20);

        // when, then
        assertThat(pageCommand.page()).isEqualTo(2);
        assertThat(pageCommand.size()).isEqualTo(20);
    }

    @Test
    @DisplayName("size_가_0이하면_1로_보정된다")
    void adjustSizeIfZeroOrLessToOne() {
        // given
        PageCommand pageCommand = PageCommand.of(0, 0);

        // when, then
        assertThat(pageCommand.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("size_가_MAX_SIZE_를_초과하면_MAX_SIZE_로_보정된다")
    void adjustSizeIfExceedsMaxSizeToMaxSize() {
        // given
        PageCommand pageCommand = PageCommand.of(0, 1000);

        // when, then
        assertThat(pageCommand.size()).isEqualTo(100);
    }

    @Test
    @DisplayName("page_가_음수이면_0으로_보정된다")
    void adjustPageIfNegativeToZero() {
        // given
        PageCommand pageCommand = PageCommand.of(-1, 10);

        // when, then
        assertThat(pageCommand.page()).isEqualTo(0);
    }

    @Test
    @DisplayName("page_와_size_가_모두_음수이면_각_보정값으로_보정된다")
    void adjustPageAndSizeIfBothNegativeToDefaultValues() {
        // given
        PageCommand pageCommand = PageCommand.of(-5, -10);

        // when, then
        assertThat(pageCommand.page()).isEqualTo(0);
        assertThat(pageCommand.size()).isEqualTo(1);
    }

}
