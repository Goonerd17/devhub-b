package teamdevhub.devhub.project.core.project.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teamdevhub.devhub.shared.core.common.exception.BusinessRuleException;
import teamdevhub.devhub.project.core.project.domain.Project;
import teamdevhub.devhub.project.core.project.port.in.usecase.ProjectMemberUseCase;
import teamdevhub.devhub.project.core.project.port.out.ProjectMemberRepository;
import teamdevhub.devhub.project.core.project.port.out.ProjectRepository;
import teamdevhub.devhub.shared.enums.ErrorCode;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProjectMemberService implements ProjectMemberUseCase {

    private final ProjectRepository projectRepository;
    private final ProjectMemberRepository projectMemberRepository;

    @Override
    public void validateReviewable(String projectGuid, String reviewerGuid, String revieweeGuid) {

        Project project = projectRepository.getProjectDetail(projectGuid);

        validateProjectCompleted(project);
        validateMember(projectGuid, reviewerGuid);
        validateMember(projectGuid, revieweeGuid);
    }

    private void validateProjectCompleted(Project project) {
        if (!project.isProgressCompleted()) {
            throw BusinessRuleException.of(ErrorCode.PROJECT_NOT_COMPLETED);
        }
    }

    private void validateMember(String projectGuid, String userGuid) {
        if (!projectMemberRepository.isMember(projectGuid, userGuid)) {
            throw BusinessRuleException.of(ErrorCode.REVIEW_NOT_A_MEMBER);
        }
    }
}
