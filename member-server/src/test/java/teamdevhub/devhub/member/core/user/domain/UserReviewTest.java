package teamdevhub.devhub.member.core.user.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import teamdevhub.devhub.shared.core.common.exception.DomainRuleException;
import teamdevhub.devhub.member.core.user.domain.UserReview;
import teamdevhub.devhub.member.core.user.port.in.command.ReviewUserCommand;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UserReviewTest {

    private static final String TEST_REVIEW_GUID_1 = "REVIEW1a1b2c3d4e5f6g7h8i9j10k11";
    private static final String TEST_PROJECT_GUID_1 = "PROJECT1a1b2c3d4e5f6g7h8i9j10k";
    private static final String TEST_USER_GUID_1 = "USER1a1b2c3d4e5f6g7h8i9j10k11l12";
    private static final String TEST_USER_GUID_2 = "USER2a1b2c3d4e5f6g7h8i9j10k11l12";

    private ReviewUserCommand commandWith(double score) {
        return ReviewUserCommand.builder()
                .userReviewGuid(TEST_REVIEW_GUID_1)
                .projectGuid(TEST_PROJECT_GUID_1)
                .reviewerGuid(TEST_USER_GUID_1)
                .revieweeGuid(TEST_USER_GUID_2)
                .score(score)
                .build();
    }

    @ParameterizedTest
    @ValueSource(doubles = {1.0, 1.5, 2.0, 2.5, 3.0, 3.5, 4.0, 4.5, 5.0})
    @DisplayName("0.5_단위의_유효한_점수로_리뷰를_생성할_수_있다")
    void create_validHalfIncrementScores_success(double score) {
        // given
        ReviewUserCommand reviewUserCommand = commandWith(score);

        // when
        UserReview review = UserReview.create("", reviewUserCommand);

        // then
        assertThat(review.getScore()).isEqualTo(score);
    }

    @Test
    @DisplayName("점수_1.0_미만이면_예외가_발생한다")
    void create_scoreBelowMin_throwsException() {
        // given
        ReviewUserCommand reviewUserCommand = commandWith(0.5);

        // when, then
        assertThatThrownBy(() -> UserReview.create(TEST_REVIEW_GUID_1, reviewUserCommand))
                .isInstanceOf(DomainRuleException.class);
    }

    @Test
    @DisplayName("점수_5.0_초과이면_예외가_발생한다")
    void create_scoreAboveMax_throwsException() {
        // given
        ReviewUserCommand reviewUserCommand = commandWith(5.5);

        // when, then
        assertThatThrownBy(() -> UserReview.create(TEST_REVIEW_GUID_1, reviewUserCommand))
                .isInstanceOf(DomainRuleException.class);
    }

    @ParameterizedTest
    @ValueSource(doubles = {1.3, 2.7, 3.1, 4.9})
    @DisplayName("0.5_단위가_아닌_점수면_예외가_발생한다")
    void create_invalidIncrement_throwsException(double score) {
        // given
        ReviewUserCommand reviewUserCommand = commandWith(score);

        // when, then
        assertThatThrownBy(() -> UserReview.create(TEST_REVIEW_GUID_1, reviewUserCommand))
                .isInstanceOf(DomainRuleException.class);
    }

    @Test
    @DisplayName("자기_자신에게_리뷰하면_예외가_발생한다")
    void create_selfReview_throwsException() {
        // given
        ReviewUserCommand selfReviewCommand = ReviewUserCommand.builder()
                .userReviewGuid(TEST_REVIEW_GUID_1)
                .projectGuid(TEST_PROJECT_GUID_1)
                .reviewerGuid(TEST_USER_GUID_1)
                .revieweeGuid(TEST_USER_GUID_1)
                .score(3.0)
                .build();

        // when, then
        assertThatThrownBy(() -> UserReview.create(TEST_REVIEW_GUID_1, selfReviewCommand))
                .isInstanceOf(DomainRuleException.class);
    }
}
