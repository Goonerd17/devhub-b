package teamdevhub.devhub.community.core.report.port.out;

import teamdevhub.devhub.community.core.report.domain.Report;

public interface ReportRepository {

    boolean existsDuplicate(String reporterUser, String boardGuid, String commentGuid);

    void save(Report report);

    void markProcessed(String reportGuid);
}
