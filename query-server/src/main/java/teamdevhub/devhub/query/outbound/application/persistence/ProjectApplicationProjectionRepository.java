package teamdevhub.devhub.query.outbound.application.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectApplicationProjectionRepository extends JpaRepository<ProjectApplicationProjectionEntity, String> {
    boolean existsByLastEventId(String eventId);
}
