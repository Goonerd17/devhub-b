package teamdevhub.devhub.query.core.home.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.query.core.home.domain.Banner;
import teamdevhub.devhub.query.core.home.domain.policy.BannerExposurePolicy;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class BannerExposurePolicyTest {

    @Test
    @DisplayName("?몄텧_湲곌컙_?댁쓽_諛곕꼫留??꾪꽣留곷맂??")
    void filterExposable_withinPeriod_returnsOnlyExposable() {
        // given
        LocalDate today = LocalDate.of(2026, 5, 9);

        Banner active = Banner.of("b1", "??댄?1", null, null, true, true,
                LocalDate.of(2026, 5, 1), LocalDate.of(2026, 5, 31), 1);
        Banner expired = Banner.of("b2", "??댄?2", null, null, true, true,
                LocalDate.of(2026, 4, 1), LocalDate.of(2026, 4, 30), 2);
        Banner notStarted = Banner.of("b3", "??댄?3", null, null, true, true,
                LocalDate.of(2026, 6, 1), LocalDate.of(2026, 6, 30), 3);

        // when
        List<Banner> result = BannerExposurePolicy.filterExposable(List.of(active, expired, notStarted), today);

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getBannerGuid()).isEqualTo("b1");
    }

    @Test
    @DisplayName("useYn??N??諛곕꼫???몄텧?섏?_?딅뒗??")
    void filterExposable_notUsed_excluded() {
        // given
        LocalDate today = LocalDate.of(2026, 5, 9);
        Banner notUsed = Banner.of("b1", "??댄?", null, null, true, false,
                LocalDate.of(2026, 5, 1), LocalDate.of(2026, 5, 31), 1);

        // when
        List<Banner> result = BannerExposurePolicy.filterExposable(List.of(notUsed), today);

        // then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("?몄텧_湲곌컙??null??諛곕꼫???몄텧?섏?_?딅뒗??")
    void filterExposable_nullDates_excluded() {
        // given
        LocalDate today = LocalDate.of(2026, 5, 9);
        Banner nullDates = Banner.of("b1", "??댄?", null, null, true, true, null, null, 1);

        // when
        List<Banner> result = BannerExposurePolicy.filterExposable(List.of(nullDates), today);

        // then
        assertThat(result).isEmpty();
    }
}
