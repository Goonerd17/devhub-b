package teamdevhub.devhub.project.core.application.port.out;

import java.util.List;
import java.util.Map;

import teamdevhub.devhub.project.core.application.domain.ProjectApplication;
import teamdevhub.devhub.project.core.application.domain.ProjectApplicationAnswer;
import teamdevhub.devhub.project.core.application.domain.ProjectApplicationScore;
import teamdevhub.devhub.platform.core.common.page.PageCommand;
import teamdevhub.devhub.platform.core.common.page.PageResult;

public interface ApplicationRepository {

	void saveApplication(ProjectApplication application);

	void saveAnswers(List<ProjectApplicationAnswer> answers);

	void updateApplicationStatus(String applicationGuid, String statusCd, String approverGuid, String decisionDate);

	void cancelApplication(String applicationGuid);

	PageResult<ProjectApplication> findApplicationsByProjectGuid(String projectGuid, PageCommand pageCommand);

	ProjectApplication findApplicationByGuid(String applicationGuid);

	List<ProjectApplicationAnswer> findAnswersByApplicationGuid(String applicationGuid);

	PageResult<ProjectApplication> findByApplicantGuid(String userGuid, PageCommand pageCommand);

	List<ProjectApplicationScore> findAcceptedByProjectGuid(String projectGuid);

	// requirementGuid별 승인된(취소되지 않은) 지원자 수 - 모집 포지션의 현재 인원 계산용
	Map<String, Long> countApprovedByRequirementGuids(List<String> requirementGuids);
}
