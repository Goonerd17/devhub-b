package teamdevhub.devhub.shared.event.project;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record ProjectCreatedPayload(String projectGuid, String ownerGuid, String title,
        String category, String username, String imageFileGuid,
        LocalDate recruitmentStartDate, LocalDate recruitmentEndDate,
        LocalDateTime registeredDate, boolean capacityClosed, String recruitStatus) {
}
