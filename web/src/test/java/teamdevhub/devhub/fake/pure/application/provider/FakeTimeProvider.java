package teamdevhub.devhub.fake.pure.application.provider;

import teamdevhub.devhub.platform.time.TimeProvider;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class FakeTimeProvider implements TimeProvider {

    private LocalDateTime currentDateTime;

    public FakeTimeProvider(LocalDateTime initialDateTime) {
        this.currentDateTime = initialDateTime;
    }

    @Override
    public LocalDateTime now() {
        return currentDateTime;
    }
    public void setNow(LocalDateTime newTime) {
        this.currentDateTime = newTime;
    }

    public void plusMinutes(long minutes) {
        this.currentDateTime = this.currentDateTime.plusMinutes(minutes);
    }

    public void plusHours(long hours) {
        this.currentDateTime = this.currentDateTime.plusHours(hours);
    }

    @Override
    public String formatDate(LocalDate date, String pattern) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String formatDateTime(LocalDateTime dateTime, String pattern) {
        throw new UnsupportedOperationException();
    }

    @Override
    public LocalDate parseDate(String dateStr, String pattern) {
        throw new UnsupportedOperationException();
    }

    @Override
    public LocalDateTime parseDateTime(String dateTimeStr, String pattern) {
        throw new UnsupportedOperationException();
    }

    @Override
    public LocalDate today() {
        return currentDateTime.toLocalDate();
    }
}
