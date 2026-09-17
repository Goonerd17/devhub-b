package teamdevhub.devhub.member.core.user.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teamdevhub.devhub.shared.core.common.exception.BusinessRuleException;
import teamdevhub.devhub.shared.identifier.IdentifierProvider;
import teamdevhub.devhub.member.core.user.domain.UserReview;
import teamdevhub.devhub.member.core.user.port.in.command.ReviewUserCommand;
import teamdevhub.devhub.member.core.user.port.in.usecase.UserReviewUseCase;
import teamdevhub.devhub.member.core.user.port.out.UserReviewRepository;
import teamdevhub.devhub.member.api.review.MemberReviewScoreQuery;
import teamdevhub.devhub.shared.shared.enums.ErrorCode;

@Service
@Transactional
@RequiredArgsConstructor
public class UserReviewService implements UserReviewUseCase, MemberReviewScoreQuery {

    private final IdentifierProvider identifierProvider;
    private final UserReviewRepository userReviewRepository;

    @Override
    @Transactional(readOnly = true)
    public java.util.Optional<Double> findScore(String projectGuid, String revieweeGuid) {
        return userReviewRepository.findScore(projectGuid, revieweeGuid);
    }

    @Override
    public double reviewMember(ReviewUserCommand reviewUserCommand) {

        validateDuplicateReview(reviewUserCommand);
        UserReview userReview = UserReview.create(identifierProvider.generateIdentifier(), reviewUserCommand);
        userReviewRepository.save(userReview);
        return userReview.getScore();
    }

    private void validateDuplicateReview(ReviewUserCommand reviewUserCommand) {
        boolean alreadyReviewed = userReviewRepository.existsByProjectGuidAndReviewerAndReviewee(
                reviewUserCommand.projectGuid(),
                reviewUserCommand.reviewerGuid(),
                reviewUserCommand.revieweeGuid()
        );

        if (alreadyReviewed) {
            throw BusinessRuleException.of(ErrorCode.REVIEW_DUPLICATE);
        }
    }
}
