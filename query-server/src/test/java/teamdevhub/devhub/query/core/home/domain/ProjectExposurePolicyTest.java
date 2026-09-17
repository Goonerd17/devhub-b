package teamdevhub.devhub.query.core.home.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.query.core.home.domain.policy.ProjectExposurePolicy;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class ProjectExposurePolicyTest {

    @Test
    @DisplayName("紐⑥쭛_湲곌컙_?댁쓽_?꾨줈?앺듃???쒖꽦_紐⑥쭛_?곹깭?대떎")
    void isActiveRecruitment_withinPeriod_returnsTrue() {
        // given
        LocalDate today = LocalDate.of(2026, 5, 9);
        LocalDate start = LocalDate.of(2026, 5, 1);
        LocalDate end = LocalDate.of(2026, 5, 31);

        // when
        boolean result = ProjectExposurePolicy.isActiveRecruitment(start, end, today);

        // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("紐⑥쭛_湲곌컙_?댄썑???꾨줈?앺듃???쒖꽦_紐⑥쭛_?곹깭媛_?꾨땲??")
    void isActiveRecruitment_afterPeriod_returnsFalse() {
        // given
        LocalDate today = LocalDate.of(2026, 6, 1);
        LocalDate start = LocalDate.of(2026, 5, 1);
        LocalDate end = LocalDate.of(2026, 5, 31);

        // when
        boolean result = ProjectExposurePolicy.isActiveRecruitment(start, end, today);

        // then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("紐⑥쭛_?좎쭨媛_null?대㈃_?쒖꽦_紐⑥쭛_?곹깭媛_?꾨땲??")
    void isActiveRecruitment_nullDates_returnsFalse() {
        // given
        LocalDate today = LocalDate.of(2026, 5, 9);

        // when
        boolean result = ProjectExposurePolicy.isActiveRecruitment(null, null, today);

        // then
        assertThat(result).isFalse();
    }
}
