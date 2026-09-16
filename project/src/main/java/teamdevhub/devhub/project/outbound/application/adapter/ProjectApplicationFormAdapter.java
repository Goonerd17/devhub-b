package teamdevhub.devhub.project.outbound.application.adapter;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import teamdevhub.devhub.project.core.application.domain.ProjectApplicationForm;
import teamdevhub.devhub.project.core.application.port.in.command.CreateProjectApplicationFormCommand;
import teamdevhub.devhub.project.core.application.port.out.ProjectApplicationFormRepository;
import teamdevhub.devhub.platform.identifier.IdentifierProvider;
import teamdevhub.devhub.project.outbound.application.adapter.entity.ProjectApplicationFormEntity;
import teamdevhub.devhub.project.outbound.application.adapter.mapper.ProjectApplicationFormMapper;
import teamdevhub.devhub.project.outbound.application.persistence.JpaProjectApplicationFormRepository;

@Component
@RequiredArgsConstructor
public class ProjectApplicationFormAdapter implements ProjectApplicationFormRepository {
	
	private final IdentifierProvider identifierProvider;
	private final JpaProjectApplicationFormRepository jpaProjectApplicationFormRepository;

	@Override
	public void saveAll(Set<CreateProjectApplicationFormCommand> formCommands) {
		List<ProjectApplicationFormEntity> entityList = formCommands.stream()
				.map(formCommand -> {
					String projectApplicationFormGuid = identifierProvider.generateIdentifier();
					ProjectApplicationForm projectApplicationForm = ProjectApplicationForm.createProjectApplicationForm(projectApplicationFormGuid, formCommand);
					return ProjectApplicationFormMapper.toEntity(projectApplicationForm);
				})
				.toList();
		jpaProjectApplicationFormRepository.saveAll(entityList);
	}

	@Override
	public void deleteByProjectGuid(String projectGuid) {
		jpaProjectApplicationFormRepository.deleteAllByProjectGuid(projectGuid);
	}

	@Override
	public List<String> findAllGuidByProjectGuid(String projectGuid) {
		return jpaProjectApplicationFormRepository.findAllGuidByProjectGuid(projectGuid);
	}

	@Override
	public List<ProjectApplicationForm> findByProjectGuid(String projectGuid) {
		return jpaProjectApplicationFormRepository.findByProjectGuid(projectGuid).stream()
				.map(ProjectApplicationFormMapper::toProjectApplicationForm)
				.toList();
	}
}
