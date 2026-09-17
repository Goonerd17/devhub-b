package teamdevhub.devhub.query.outbound.home.persistence;

import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface HomeProjectQueryDao {

    List<HomeProjectDto> findHomeProjects(LocalDate today, Pageable pageable);

    record HomeProjectDto(
            String projectGuid,
            String title,
            String category,
            String username,
            String imageFileGuid,
            LocalDate recruitmentStartDate,
            LocalDate recruitmentEndDate,
            LocalDateTime registeredDate,
            boolean capacityClosed
    ) {}
}
