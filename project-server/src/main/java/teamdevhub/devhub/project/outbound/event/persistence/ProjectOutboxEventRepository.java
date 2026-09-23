package teamdevhub.devhub.project.outbound.event.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import jakarta.persistence.LockModeType;

import java.util.List;
import java.time.Instant;

public interface ProjectOutboxEventRepository extends JpaRepository<ProjectOutboxEventEntity, String> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<ProjectOutboxEventEntity> findTop100ByPublishedAtIsNullOrderByOccurredAtAsc();

    long deleteByPublishedAtBefore(Instant cutoff);
}
