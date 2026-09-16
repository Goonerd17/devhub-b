package teamdevhub.devhub.platform.time;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface TimeProvider {

    LocalDateTime now();
    LocalDate today();
    String formatDate(LocalDate date, String pattern);
    String formatDateTime(LocalDateTime dateTime, String pattern);
    LocalDate parseDate(String dateStr, String pattern);
    LocalDateTime parseDateTime(String dateTimeStr, String pattern);
}
