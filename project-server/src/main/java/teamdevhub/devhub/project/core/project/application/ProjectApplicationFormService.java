package teamdevhub.devhub.project.core.project.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import teamdevhub.devhub.project.core.application.domain.ProjectApplicationForm;
import teamdevhub.devhub.project.core.application.port.out.ProjectApplicationFormRepository;
import teamdevhub.devhub.project.core.project.port.in.usecase.ProjectApplicationFormUseCase;

@Service
@Transactional
@RequiredArgsConstructor
public class ProjectApplicationFormService implements ProjectApplicationFormUseCase {
	
	private final ProjectApplicationFormRepository projectApplicationFormRepository;

	@Override
	public List<String> getByProjectGuid(String projectGuid) {
		return projectApplicationFormRepository.findAllGuidByProjectGuid(projectGuid);
	}

	@Override
	public List<ProjectApplicationForm> findByProjectGuid(String projectGuid) {
		return projectApplicationFormRepository.findByProjectGuid(projectGuid);
	}
}
