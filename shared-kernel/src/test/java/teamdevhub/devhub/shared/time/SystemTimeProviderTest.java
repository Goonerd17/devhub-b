package teamdevhub.devhub.shared.time;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

class SystemTimeProviderTest {

    private final SystemTimeProvider provider = new SystemTimeProvider();

    @Test
    void formatAndParseDateReturnsOriginal() {
        LocalDate date = LocalDate.of(2026, 1, 9);

        String formatted = provider.formatDate(date, "yyyy-MM-dd");

        assertEquals("2026-01-09", formatted);
        assertEquals(date, provider.parseDate(formatted, "yyyy-MM-dd"));
    }

    @Test
    void formatAndParseDateTimeReturnsOriginal() {
        LocalDateTime dateTime = LocalDateTime.of(2026, 1, 9, 14, 45, 30);

        String formatted = provider.formatDateTime(dateTime, "yyyy-MM-dd HH:mm:ss");

        assertEquals("2026-01-09 14:45:30", formatted);
        assertEquals(dateTime, provider.parseDateTime(formatted, "yyyy-MM-dd HH:mm:ss"));
    }

    @Test
    void todayAndNowReturnCurrentDateTime() {
        assertNotNull(provider.today());
        assertNotNull(provider.now());
    }

    @Test
    void formatDateReturnsEmptyStringForNullInput() {
        assertEquals("", provider.formatDate(null, "yyyy-MM-dd"));
    }

    @Test
    void formatDateTimeReturnsEmptyStringForNullInput() {
        assertEquals("", provider.formatDateTime(null, "yyyy-MM-dd HH:mm:ss"));
    }

    @Test
    void parseDateReturnsNullForNullEmptyOrBlankInput() {
        assertNull(provider.parseDate(null, "yyyy-MM-dd"));
        assertNull(provider.parseDate("", "yyyy-MM-dd"));
        assertNull(provider.parseDate("   ", "yyyy-MM-dd"));
    }

    @Test
    void parseDateTimeReturnsNullForNullEmptyOrBlankInput() {
        assertNull(provider.parseDateTime(null, "yyyy-MM-dd HH:mm:ss"));
        assertNull(provider.parseDateTime("", "yyyy-MM-dd HH:mm:ss"));
        assertNull(provider.parseDateTime("   ", "yyyy-MM-dd HH:mm:ss"));
    }
}
