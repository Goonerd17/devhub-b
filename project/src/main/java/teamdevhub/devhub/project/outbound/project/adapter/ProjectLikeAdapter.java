package teamdevhub.devhub.project.outbound.project.adapter;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import teamdevhub.devhub.platform.core.common.page.PageCommand;
import teamdevhub.devhub.platform.core.common.page.PageResult;
import teamdevhub.devhub.project.core.project.domain.ProjectLike;
import teamdevhub.devhub.project.core.project.port.out.ProjectLikeRepository;
import teamdevhub.devhub.project.outbound.project.adapter.entity.ProjectLikeEntity;
import teamdevhub.devhub.project.outbound.project.adapter.mapper.ProjectLikeMapper;
import teamdevhub.devhub.project.outbound.project.adapter.mapper.ProjectMapper;
import teamdevhub.devhub.project.outbound.project.persistence.JpaProjectLikeRepository;

@Component
@RequiredArgsConstructor
public class ProjectLikeAdapter implements ProjectLikeRepository {
	
	private final JpaProjectLikeRepository jpaProjectLikeRepository;

	@Override
	public void save(ProjectLike projectLike) {
		ProjectLikeEntity entity = ProjectLikeMapper.toEntity(projectLike);
		jpaProjectLikeRepository.save(entity);
	}

	@Override
	public ProjectLike findByProjectGuidAndUserGuid(String projectGuid, String userGuid) {
		ProjectLikeEntity entity = jpaProjectLikeRepository.findByProjectGuidAndUserGuid(projectGuid, userGuid);
		return ProjectLikeMapper.toProjectLike(entity);
	}
	
	@Override
	public List<ProjectLike> findByProjectGuid(Set<String> projectGuids) {
		List<ProjectLikeEntity> entityList = jpaProjectLikeRepository.findByProjectGuids(projectGuids);
		return entityList.stream()
				.map(ProjectMapper::toProjectLike)
				.toList();
	}

	@Override
	public int countByProjectGuid(String projectGuid) {
		return jpaProjectLikeRepository.countByProjectGuid(projectGuid);
	}

	@Override
	public void deleteByProjectGuid(String projectGuid) {
		jpaProjectLikeRepository.deleteAllByProjectGuid(projectGuid);
	}

	@Override
	public void deleteById(String projectLikeGuid) {
		jpaProjectLikeRepository.deleteById(projectLikeGuid);
	}

	@Override
	public PageResult<ProjectLike> findByUserGuid(String userGuid, PageCommand pageCommand) {
		Pageable pageable = PageRequest.of(pageCommand.page(), pageCommand.size());
		Page<ProjectLikeEntity> pagedProjectLike = jpaProjectLikeRepository.findByUserGuid(userGuid, pageable);
		return PageResult.of(
				pagedProjectLike.getContent().stream().map(ProjectLikeMapper::toProjectLike).toList(),
				pagedProjectLike.getNumber(),
				pagedProjectLike.getSize(),
				pagedProjectLike.getTotalElements());
	}

}
