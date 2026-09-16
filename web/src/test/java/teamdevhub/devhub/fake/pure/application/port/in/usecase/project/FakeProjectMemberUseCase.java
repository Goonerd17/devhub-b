package teamdevhub.devhub.fake.pure.application.port.in.usecase.project;

import teamdevhub.devhub.project.core.project.port.in.usecase.ProjectMemberUseCase;

public class FakeProjectMemberUseCase implements ProjectMemberUseCase {

    public boolean called = false;

    public String projectGuid;
    public String reviewerGuid;
    public String revieweeGuid;

    private RuntimeException exception;

    @Override
    public void validateReviewable(String projectGuid, String reviewerGuid, String revieweeGuid) {
        this.called = true;
        this.projectGuid = projectGuid;
        this.reviewerGuid = reviewerGuid;
        this.revieweeGuid = revieweeGuid;

        if (exception != null) {
            throw exception;
        }
    }

    public void willThrow(RuntimeException e) {
        this.exception = e;
    }
}

