package teamdevhub.devhub.member.core.user.domain;

import lombok.Builder;
import lombok.Getter;
import teamdevhub.devhub.shared.core.common.exception.DomainRuleException;
import teamdevhub.devhub.member.core.user.port.in.command.ReviewUserCommand;
import teamdevhub.devhub.shared.shared.enums.ErrorCode;

import java.util.Set;

@Getter
public class UserReview {

    private static final double MIN_SCORE = 1.0;
    private static final double MAX_SCORE = 5.0;
    private static final Set<Double> VALID_SCORES =
            Set.of(1.0, 1.5, 2.0, 2.5, 3.0, 3.5, 4.0, 4.5, 5.0);

    private final String userReviewGuid;
    private final String projectGuid;
    private final String reviewer;
    private final String reviewee;
    private final double score;

    @Builder
    private UserReview(
            String userReviewGuid,
            String projectGuid,
            String reviewer,
            String reviewee,
            double score
    ) {
        this.userReviewGuid = userReviewGuid;
        this.projectGuid = projectGuid;
        this.reviewer = reviewer;
        this.reviewee = reviewee;
        this.score = score;
    }

    public static UserReview create(
            String userReviewGuid,
            ReviewUserCommand reviewUserCommand
    ) {
        validateReviewer(reviewUserCommand.reviewerGuid(), reviewUserCommand.revieweeGuid());
        validateScore(reviewUserCommand.score());

        return UserReview.builder()
                .userReviewGuid(userReviewGuid)
                .projectGuid(reviewUserCommand.projectGuid())
                .reviewer(reviewUserCommand.reviewerGuid())
                .reviewee(reviewUserCommand.revieweeGuid())
                .score(reviewUserCommand.score())
                .build();
    }

    private static void validateReviewer(String reviewer, String reviewee) {
        if (reviewer.equals(reviewee)) {
            throw DomainRuleException.of(ErrorCode.REVIEW_SELF_NOT_ALLOWED);
        }
    }

    private static void validateScore(double score) {
        validateScoreRange(score);
        validateScoreStep(score);
    }

    private static void validateScoreRange(double score) {
        if (score < MIN_SCORE || score > MAX_SCORE) {
            throw DomainRuleException.of(ErrorCode.REVIEW_SCORE_INVALID);
        }
    }

    private static void validateScoreStep(double score) {
        if (!VALID_SCORES.contains(score)) {
            throw DomainRuleException.of(ErrorCode.REVIEW_SCORE_INVALID);
        }
    }
}