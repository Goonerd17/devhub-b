package teamdevhub.devhub.fake.pure.application.port.out.user;

import teamdevhub.devhub.member.core.user.domain.UserReview;
import teamdevhub.devhub.member.core.user.port.out.UserReviewRepository;

import java.util.ArrayList;
import java.util.List;

public class FakeUserReviewRepository implements UserReviewRepository {

    private final List<UserReview> store = new ArrayList<>();

    @Override
    public void save(UserReview userReview) {
        store.add(userReview);
    }

    @Override
    public boolean existsByProjectGuidAndReviewerAndReviewee(String projectGuid, String reviewer, String reviewee) {
        return store.stream().anyMatch(r ->
                projectGuid.equals(r.getProjectGuid())
                        && reviewer.equals(r.getReviewer())
                        && reviewee.equals(r.getReviewee()));
    }

    public List<UserReview> findAll() {
        return List.copyOf(store);
    }
}
