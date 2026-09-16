package teamdevhub.devhub.fake.pure.application.port.out.application;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import teamdevhub.devhub.project.core.application.domain.ProjectApplicationForm;
import teamdevhub.devhub.project.core.application.port.in.command.CreateProjectApplicationFormCommand;
import teamdevhub.devhub.project.core.application.port.out.ProjectApplicationFormRepository;
import teamdevhub.devhub.fake.pure.application.provider.FakeUuidIdentifierProvider;

public class FakeProjectApplicationFormRepository implements ProjectApplicationFormRepository {
	
	private final FakeUuidIdentifierProvider identifierProvider = new FakeUuidIdentifierProvider("PROJECT_APPLICATION_FORM_UUID");
	private final List<ProjectApplicationForm> store = new ArrayList<>();

	@Override
	public void saveAll(Set<CreateProjectApplicationFormCommand> forms) {
		List<ProjectApplicationForm> projectApplicationFormList = forms.stream()
				.map(form -> {
					String uuid = identifierProvider.generateIdentifier();
					return ProjectApplicationForm.createProjectApplicationForm(uuid, form);
				})
				.toList();
		store.addAll(projectApplicationFormList);
	}

	@Override
	public void deleteByProjectGuid(String projectGuid) {

	}

	@Override
	public List<String> findAllGuidByProjectGuid(String projectGuid) {
		return List.of();
	}

	public List<ProjectApplicationForm> findByProjectGuid(String projectGuid) {
		return store.stream()
				.filter(item -> projectGuid.equals(item.getProjectGuid()))
				.toList();
	}

}
