package teamdevhub.devhub.member.core.user.port.in.usecase;

import teamdevhub.devhub.member.core.user.port.in.command.ReviewUserCommand;

public interface UserReviewUseCase {

    double reviewMember(ReviewUserCommand command);
}
