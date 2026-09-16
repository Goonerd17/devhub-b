package teamdevhub.devhub.member.api.review;

import java.util.Optional;

public interface MemberReviewScoreQuery {
    Optional<Double> findScore(String projectGuid, String revieweeGuid);
}
