package teamdevhub.devhub.project.outbound.project.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import teamdevhub.devhub.platform.identifier.IdentifierProvider;
import teamdevhub.devhub.project.core.project.domain.ProjectRequirement;
import teamdevhub.devhub.project.core.project.domain.vo.command.CreateProjectRequirementCommand;
import teamdevhub.devhub.project.core.project.port.out.ProjectRequirementRepository;
import teamdevhub.devhub.project.outbound.project.adapter.entity.ProjectRequirementEntity;
import teamdevhub.devhub.project.outbound.project.adapter.mapper.ProjectRequirementMapper;
import teamdevhub.devhub.project.outbound.project.persistence.JpaProjectRequirementRepository;

@Component
@RequiredArgsConstructor
public class ProjectRecruitementAdapter implements ProjectRequirementRepository {
	
	private final IdentifierProvider identifierProvider;
	private final JpaProjectRequirementRepository jpaProjectRequirementRepository;
	@Override
	public void saveAll(Set<CreateProjectRequirementCommand> positions) {
		if(positions.isEmpty()) {
			return;
		}

		List<ProjectRequirementEntity> projectRequirementEntityList = positions.stream()
				.map(projectRequirementCommand -> {
					String projectRequirementGuid = identifierProvider.generateIdentifier();
					ProjectRequirement projectRequirement = ProjectRequirement.createProjectRequirement(projectRequirementGuid, projectRequirementCommand);
					return ProjectRequirementMapper.toEntity(projectRequirement);
				})
				.toList();
		jpaProjectRequirementRepository.saveAll(projectRequirementEntityList);

	}
	@Override
	public List<String> selectProjectGuidByPositionCodeAndPositionLevel(List<String> positionCodeList,
			List<String> positionLevelCodeList) {
		List<String> projectGuids = new ArrayList<>();
		if(positionCodeList == null && positionLevelCodeList == null) {
			return projectGuids;
		}
		return jpaProjectRequirementRepository.findByPositionCdAndLevelCd(positionCodeList, positionLevelCodeList);
	}
	@Override
	public List<ProjectRequirement> findByProjectGuids(Set<String> projectGuids) {
		List<ProjectRequirementEntity> entityList = jpaProjectRequirementRepository.findByProjectGuids(projectGuids);
		return entityList.stream()
				.map(ProjectRequirementMapper::toProjectRequirement)
				.toList();
	}
	@Override
	public List<ProjectRequirement> findByProjectGuid(String projectGuid) {
		List<ProjectRequirementEntity> entityList = jpaProjectRequirementRepository.findByProjectGuid(projectGuid);
		return entityList.stream()
				.map(ProjectRequirementMapper::toProjectRequirement)
				.toList();
	}
	@Override
	public void deleteByProjectGuid(String projectGuid) {
		jpaProjectRequirementRepository.deleteAllByProjectGuid(projectGuid);
	}
	@Override
	public ProjectRequirement findById(String projectRequiementGuid) {
		ProjectRequirementEntity entity = jpaProjectRequirementRepository.findById(projectRequiementGuid).get();
		return ProjectRequirementMapper.toProjectRequirement(entity);
	}

}
