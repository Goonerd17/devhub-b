package teamdevhub.devhub.shared.event.project;

import java.time.LocalDate;

public record ProjectUpdatedPayload(String projectGuid, String title, String category,
        String username, String imageFileGuid, LocalDate recruitmentStartDate,
        LocalDate recruitmentEndDate, boolean capacityClosed, String recruitStatus) {
}
