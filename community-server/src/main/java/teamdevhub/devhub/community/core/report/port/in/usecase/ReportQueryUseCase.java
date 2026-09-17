package teamdevhub.devhub.community.core.report.port.in.usecase;

import teamdevhub.devhub.shared.core.common.page.PageCommand;
import teamdevhub.devhub.shared.core.common.page.PageResult;
import teamdevhub.devhub.community.core.report.domain.Report;

public interface ReportQueryUseCase {

    PageResult<Report> getReportsByReportedUser(String userGuid, PageCommand pageCommand);
    PageResult<Report> getReportsByReporterUser(String userGuid, PageCommand pageCommand);
    PageResult<Report> getAllReports(PageCommand pageCommand);
}
