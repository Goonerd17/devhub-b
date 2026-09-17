package teamdevhub.devhub.project.outbound.project.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import teamdevhub.devhub.project.core.project.port.out.ProjectMemberRepository;
import teamdevhub.devhub.project.outbound.application.persistence.JpaProjectApplicationRepository;
import teamdevhub.devhub.project.outbound.project.adapter.entity.ProjectRequirementEntity;
import teamdevhub.devhub.project.outbound.project.persistence.JpaProjectRepository;
import teamdevhub.devhub.project.outbound.project.persistence.JpaProjectRequirementRepository;
import teamdevhub.devhub.project.core.project.domain.ProjectApprovalStatus;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ProjectMemberAdapter implements ProjectMemberRepository {

    private final JpaProjectRepository jpaProjectRepository;
    private final JpaProjectRequirementRepository jpaProjectRequirementRepository;
    private final JpaProjectApplicationRepository jpaProjectApplicationRepository;

    @Override
    public boolean isMember(String projectGuid, String userGuid) {
        return isProjectOwner(projectGuid, userGuid) || isApprovedApplicant(projectGuid, userGuid);
    }

    private boolean isProjectOwner(String projectGuid, String userGuid) {
        return jpaProjectRepository.findById(projectGuid)
                .map(entity -> userGuid.equals(entity.getUserGuid()))
                .orElse(false);
    }

    private boolean isApprovedApplicant(String projectGuid, String userGuid) {
        List<String> requirementGuids = jpaProjectRequirementRepository
                .findByProjectGuid(projectGuid).stream()
                .map(ProjectRequirementEntity::getProjectRequirementGuid)
                .toList();

        if (requirementGuids.isEmpty()) {
            return false;
        }

        return jpaProjectApplicationRepository
                .findByRequirementGuidIn(requirementGuids, PageRequest.of(0, Integer.MAX_VALUE))
                .stream()
                .anyMatch(app -> userGuid.equals(app.getApplicantGuid())
                        && ProjectApprovalStatus.APPROVED.getCode().equals(app.getStatusCd())
                        && !app.isCanceled());
    }
}
