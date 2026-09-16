package teamdevhub.devhub.fake.pure.application.port.out.project;

import teamdevhub.devhub.project.core.project.domain.ProjectRequirement;
import teamdevhub.devhub.project.core.project.domain.vo.command.CreateProjectRequirementCommand;
import teamdevhub.devhub.project.core.project.port.out.ProjectRequirementRepository;
import teamdevhub.devhub.fake.pure.application.provider.FakeUuidIdentifierProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class FakeProjectRequirementRepository implements ProjectRequirementRepository {
	
	private final FakeUuidIdentifierProvider identifierProvider = new FakeUuidIdentifierProvider("PROJECT_REQUIREMENT_UUID");
	private final List<ProjectRequirement> store = new ArrayList<>();

	@Override
	public void saveAll(Set<CreateProjectRequirementCommand> positions) {
		List<ProjectRequirement> projectRequirementList = positions.stream()
				.map(item -> {
					String uuid = identifierProvider.generateIdentifier();
					return ProjectRequirement.createProjectRequirement(uuid, item);
				}).toList();
		store.addAll(projectRequirementList);
	}

	@Override
	public List<String> selectProjectGuidByPositionCodeAndPositionLevel(List<String> positionCodeList, List<String> positionLevelCodeList) {
		return List.of();
	}

	@Override
	public List<ProjectRequirement> findByProjectGuids(Set<String> projectGuids) {
		return List.of();
	}

	public List<ProjectRequirement> findByProjectGuid(String projectGuid) {
		return store.stream()
				.filter(item -> projectGuid.equals(item.getProjectGuid()))
				.toList();
	}

	@Override
	public void deleteByProjectGuid(String projectGuid) {

	}

	@Override
	public ProjectRequirement findById(String projectRequirementGuid) {
		return null;
	}

}
