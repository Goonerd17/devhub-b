package teamdevhub.devhub.fake.pure.application.port.in.usecase.user;

import teamdevhub.devhub.member.core.user.port.in.command.ReviewUserCommand;
import teamdevhub.devhub.member.core.user.port.in.usecase.UserReviewUseCase;

public class FakeUserReviewUseCase implements UserReviewUseCase {

    public boolean called = false;
    public ReviewUserCommand lastCommand;

    private double returnScore = 0.0;

    @Override
    public double reviewMember(ReviewUserCommand command) {
        this.called = true;
        this.lastCommand = command;
        return returnScore;
    }

    public void willReturn(double score) {
        this.returnScore = score;
    }
}
