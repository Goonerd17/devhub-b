package teamdevhub.devhub.query.outbound.project.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface ProjectProjectionRepository extends JpaRepository<ProjectProjectionEntity, String> {
    boolean existsByLastEventId(String eventId);

    default List<ProjectProjectionEntity> findRecent(int limit) {
        return findAll(Sort.by(Sort.Direction.DESC, "registeredDate")).stream()
                .filter(project -> "3201".equals(project.getRecruitStatus()))
                .limit(limit)
                .toList();
    }
}
