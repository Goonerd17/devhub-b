package teamdevhub.devhub.shared.home;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record HomeProjectView(String projectGuid, String title, String category, String username,
        String imageFileGuid, LocalDate recruitmentStartDate, LocalDate recruitmentEndDate,
        LocalDateTime registeredDate, boolean capacityClosed, String recruitStatus) {
}
