package teamdevhub.devhub.project.outbound.event.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.time.Instant;

public interface ProjectOutboxEventRepository extends JpaRepository<ProjectOutboxEventEntity, String> {
    List<ProjectOutboxEventEntity> findTop100ByPublishedAtIsNullOrderByOccurredAtAsc();

    long deleteByPublishedAtBefore(Instant cutoff);
}
