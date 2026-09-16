package teamdevhub.devhub.readmodel.core.home.domain.policy;

import java.time.LocalDate;

public class ProjectExposurePolicy {

    private ProjectExposurePolicy() {}

    public static boolean isActiveRecruitment(LocalDate recruitmentStartDate, LocalDate recruitmentEndDate, LocalDate today) {
        if (recruitmentStartDate == null || recruitmentEndDate == null) {
            return false;
        }
        return !today.isBefore(recruitmentStartDate) && !today.isAfter(recruitmentEndDate);
    }
}
