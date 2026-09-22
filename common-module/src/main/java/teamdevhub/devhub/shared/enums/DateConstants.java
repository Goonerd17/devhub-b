package teamdevhub.devhub.shared.enums;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public enum DateConstants {
    ;
    public static final LocalDate MAX_LOCAL_DATE = LocalDate.of(2099, 12, 31);
    public static final LocalDateTime MAX_LOCAL_DATE_TIME = LocalDateTime.of(MAX_LOCAL_DATE, LocalTime.MAX);
    public static final String MAX_DATE_STRING = MAX_LOCAL_DATE.format(DateTimeFormatter.ISO_LOCAL_DATE); // 9999-12-31
    public static final String MAX_DATE_COMPACT = "20991231";
}
