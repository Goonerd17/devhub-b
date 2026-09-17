package teamdevhub.devhub.member.outbound.user.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import teamdevhub.devhub.member.core.user.domain.UserReview;
import teamdevhub.devhub.member.core.user.port.out.UserReviewRepository;
import teamdevhub.devhub.member.outbound.user.adapter.entity.UserReviewEntity;
import teamdevhub.devhub.member.outbound.user.persistence.JpaUserReviewRepository;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserReviewAdapter implements UserReviewRepository {

    private final JpaUserReviewRepository jpaUserReviewRepository;

    @Override
    public void save(UserReview userReview) {
        jpaUserReviewRepository.save(toEntity(userReview));
    }

    @Override
    public boolean existsByProjectGuidAndReviewerAndReviewee(String projectGuid, String reviewer, String reviewee) {
        return jpaUserReviewRepository.existsByProjectGuidAndReviewerAndReviewee(projectGuid, reviewer, reviewee);
    }

    private UserReviewEntity toEntity(UserReview userReview) {
        return UserReviewEntity.builder()
                .userReviewGuid(userReview.getUserReviewGuid())
                .projectGuid(userReview.getProjectGuid())
                .reviewer(userReview.getReviewer())
                .reviewee(userReview.getReviewee())
                .score(userReview.getScore())
                .build();
    }

    @Override
    public Optional<Double> findScore(String projectGuid, String reviewee) {
        return jpaUserReviewRepository.findByProjectGuidAndReviewee(projectGuid, reviewee).map(entity -> entity.getScore());
    }
}
