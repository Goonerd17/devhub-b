package teamdevhub.devhub.project.core.project.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import teamdevhub.devhub.platform.core.common.page.PageCommand;
import teamdevhub.devhub.platform.core.common.page.PageResult;
import teamdevhub.devhub.platform.identifier.IdentifierProvider;
import teamdevhub.devhub.project.core.project.domain.ProjectLike;
import teamdevhub.devhub.project.core.project.domain.vo.command.CreateProjectLikeCommand;
import teamdevhub.devhub.project.core.project.port.in.usecase.ProjectLikeUseCase;
import teamdevhub.devhub.project.core.project.port.out.ProjectLikeRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class ProjectLikeService implements ProjectLikeUseCase {
	
	private final IdentifierProvider identifierProvider;
	
	private final ProjectLikeRepository projectLikeRepository;

	@Override
	public void toggleProjectLike(CreateProjectLikeCommand createProjectLikeCommand) {
		ProjectLike existedProjectLike = projectLikeRepository.findByProjectGuidAndUserGuid(createProjectLikeCommand.projectGuid(), createProjectLikeCommand.userGuid());
		if(existedProjectLike == null) {
			ProjectLike projectLike = createGeneralProjectSkill(createProjectLikeCommand);
			projectLikeRepository.save(projectLike);
		} else {
			projectLikeRepository.deleteById(existedProjectLike.getProjectLikeGuid());
		}
	}

	@Override
	public ProjectLike findByProjectGuidAndUserGuid(String projectGuid, String userGuid) {
		return projectLikeRepository.findByProjectGuidAndUserGuid(projectGuid, userGuid);
	}
	
	private ProjectLike createGeneralProjectSkill(CreateProjectLikeCommand createProjectLikeCommand) {
		String projectLikeGuid = identifierProvider.generateIdentifier();
		return ProjectLike.createProjectLike(createProjectLikeCommand, projectLikeGuid);
	}

	@Override
	public PageResult<ProjectLike> findByUserGuid(String userGuid, PageCommand pageCommand) {
		return projectLikeRepository.findByUserGuid(userGuid, pageCommand);
	}
	
}
