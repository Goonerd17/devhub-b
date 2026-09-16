package teamdevhub.devhub.project.core.project.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teamdevhub.devhub.platform.core.common.page.PageCommand;
import teamdevhub.devhub.platform.core.common.page.PageResult;
import teamdevhub.devhub.project.core.project.domain.Project;
import teamdevhub.devhub.project.core.project.port.in.command.SearchProjectListCommand;
import teamdevhub.devhub.project.core.project.port.in.usecase.ProjectQueryUseCase;
import teamdevhub.devhub.project.core.project.port.out.ProjectQueryRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class ProjectQueryService implements ProjectQueryUseCase {

    private final ProjectQueryRepository projectQueryRepository;

    @Override
    public PageResult<Project> getProjectList(SearchProjectListCommand searchProjectListCommand, PageCommand pageCommand) {
        return projectQueryRepository.getProjectList(searchProjectListCommand, pageCommand);
    }
}
