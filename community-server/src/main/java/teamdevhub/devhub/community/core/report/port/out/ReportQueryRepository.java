package teamdevhub.devhub.community.core.report.port.out;

import teamdevhub.devhub.shared.core.common.page.PageCommand;
import teamdevhub.devhub.shared.core.common.page.PageResult;
import teamdevhub.devhub.community.core.report.domain.Report;

public interface ReportQueryRepository {

    PageResult<Report> findByReportedUser(String userGuid, PageCommand pageCommand);
    PageResult<Report> findByReporterUser(String userGuid, PageCommand pageCommand);
    PageResult<Report> findAll(PageCommand pageCommand);
}
