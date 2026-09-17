package teamdevhub.devhub.project.core.project.port.in.usecase;

public interface ProjectMemberUseCase {

    void validateReviewable(String projectGuid, String reviewerGuid, String revieweeGuid);
}