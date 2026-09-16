package teamdevhub.devhub.web.core.user.port.in.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teamdevhub.devhub.project.core.project.port.in.usecase.ProjectMemberUseCase;
import teamdevhub.devhub.member.core.user.port.in.command.ReviewUserCommand;
import teamdevhub.devhub.member.core.user.port.in.usecase.UserProfileUseCase;
import teamdevhub.devhub.member.core.user.port.in.usecase.UserReviewUseCase;

@Service
@Transactional
@RequiredArgsConstructor
public class UserReviewFacade {

    private final ProjectMemberUseCase projectMemberUseCase;
    private final UserReviewUseCase userReviewUseCase;
    private final UserProfileUseCase userProfileUseCase;

    public void reviewMember(ReviewUserCommand reviewUserCommand) {

        projectMemberUseCase.validateReviewable(reviewUserCommand.projectGuid(), reviewUserCommand.reviewerGuid(), reviewUserCommand.revieweeGuid());
        double reviewScore = userReviewUseCase.reviewMember(reviewUserCommand);
        userProfileUseCase.updateUserMannerDegree(reviewUserCommand.revieweeGuid(), reviewScore);
    }
}
