package teamdevhub.devhub.project.outbound.project.adapter;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import teamdevhub.devhub.platform.core.common.page.PageCommand;
import teamdevhub.devhub.platform.core.common.page.PageResult;
import teamdevhub.devhub.project.core.project.domain.Project;
import teamdevhub.devhub.project.core.project.port.in.command.SearchProjectListCommand;
import teamdevhub.devhub.project.core.project.port.out.ProjectQueryRepository;
import teamdevhub.devhub.project.outbound.project.adapter.entity.ProjectEntity;
import teamdevhub.devhub.project.outbound.project.adapter.mapper.ProjectMapper;
import teamdevhub.devhub.project.outbound.project.persistence.JpaProjectRepository;

@Component
@RequiredArgsConstructor
public class ProjectQueryAdapter implements ProjectQueryRepository {

    private final JpaProjectRepository jpaProjectRepository;
    
    @Override
    public PageResult<Project> getProjectList(SearchProjectListCommand searchProjectListCommand, PageCommand pageCommand) {
        Pageable pageable = PageRequest.of(pageCommand.page(), pageCommand.size(), Sort.by("registeredDate").descending());

        Page<ProjectEntity> pagedProjectList= jpaProjectRepository.findBySearchCondition(
        		searchProjectListCommand.keyword(), searchProjectListCommand.order(),
				searchProjectListCommand.skillCodeList(), searchProjectListCommand.regionCodeList(), searchProjectListCommand.positionCodeList(),
				searchProjectListCommand.positionLevelCodeList(), searchProjectListCommand.projectRecruitTypeList(),
				searchProjectListCommand.projectRecruitStatusList(), searchProjectListCommand.projectProgressTypeList(),
				searchProjectListCommand.recruitmentStartDate(), searchProjectListCommand.recruitmentEndDate(),
				searchProjectListCommand.progressStartDate(), searchProjectListCommand.progressEndDate(), searchProjectListCommand.progressPeriodList(), pageable);
        
        return PageResult.of(
        		pagedProjectList.getContent().stream().map(ProjectMapper::toProject).toList(),
        		pagedProjectList.getNumber(),
        		pagedProjectList.getSize(),
        		pagedProjectList.getTotalElements());
    }
}
