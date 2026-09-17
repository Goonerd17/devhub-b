package teamdevhub.devhub.member.core.user.port.out;

import teamdevhub.devhub.member.core.user.domain.UserReview;
import java.util.Optional;

public interface UserReviewRepository {

    void save(UserReview userReview);

    boolean existsByProjectGuidAndReviewerAndReviewee(String projectGuid, String reviewer, String reviewee);

    default Optional<Double> findScore(String projectGuid, String reviewee) { return Optional.empty(); }
}
