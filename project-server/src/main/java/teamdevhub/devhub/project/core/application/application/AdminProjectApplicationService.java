package teamdevhub.devhub.project.core.application.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import teamdevhub.devhub.project.core.application.domain.ProjectApplication;
import teamdevhub.devhub.project.core.application.domain.ProjectApplicationAnswer;
import teamdevhub.devhub.project.core.application.port.in.command.SearchAdminProjectApplicationCommand;
import teamdevhub.devhub.project.core.application.port.in.usecase.AdminProjectApplicationUseCase;
import teamdevhub.devhub.project.core.application.port.out.AdminProjectApplicationRepository;
import teamdevhub.devhub.shared.core.common.page.PageCommand;
import teamdevhub.devhub.shared.core.common.page.PageResult;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminProjectApplicationService implements AdminProjectApplicationUseCase {
	
	private final AdminProjectApplicationRepository adminProjectApplicationRepository;

	@Override
	public List<ProjectApplicationAnswer> getAnswersByApplicationGuid(String applicationGuid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PageResult<ProjectApplication> getApplicationsByProjectGuid(
			SearchAdminProjectApplicationCommand searchAdminProjectApplicationCommand, PageCommand pageCommand) {
		PageResult<ProjectApplication> result = adminProjectApplicationRepository.getApplicationsByProjectGuid(searchAdminProjectApplicationCommand, pageCommand);
		return result;
	}

	@Override
	public ProjectApplication getApplicationByGuid(String applicationGuid) {
		// TODO Auto-generated method stub
		return null;
	}



}
