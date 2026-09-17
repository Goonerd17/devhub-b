package teamdevhub.devhub.community.core.report.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import teamdevhub.devhub.shared.core.common.page.PageCommand;
import teamdevhub.devhub.shared.core.common.page.PageResult;
import teamdevhub.devhub.community.core.report.domain.Report;
import teamdevhub.devhub.community.core.report.port.in.usecase.ReportQueryUseCase;
import teamdevhub.devhub.community.core.report.port.out.ReportQueryRepository;
import teamdevhub.devhub.community.api.ReportQuery;
import teamdevhub.devhub.community.api.ReportPage;
import teamdevhub.devhub.community.api.ReportView;

@Service
@Transactional
@RequiredArgsConstructor
public class ReportQueryService implements ReportQueryUseCase, ReportQuery {

    private final ReportQueryRepository reportQueryRepository;

    @Override
    public PageResult<Report> getReportsByReportedUser(String userGuid, PageCommand pageCommand) {
        return reportQueryRepository.findByReportedUser(userGuid, pageCommand);
    }

    @Override
    public PageResult<Report> getReportsByReporterUser(String userGuid, PageCommand pageCommand) {
        return reportQueryRepository.findByReporterUser(userGuid, pageCommand);
    }

    @Override
    public PageResult<Report> getAllReports(PageCommand pageCommand) {
        return reportQueryRepository.findAll(pageCommand);
    }

    @Override public ReportPage findReceived(String userGuid, int page, int size) {
        return toPublic(getReportsByReportedUser(userGuid, new PageCommand(page, size)));
    }
    @Override public ReportPage findSubmitted(String userGuid, int page, int size) {
        return toPublic(getReportsByReporterUser(userGuid, new PageCommand(page, size)));
    }
    @Override public ReportPage findAll(int page, int size) {
        return toPublic(getAllReports(new PageCommand(page, size)));
    }
    private ReportPage toPublic(PageResult<Report> result) {
        return new ReportPage(result.content().stream().map(r -> new ReportView(r.getReportGuid(), r.getBoardGuid(), r.getCommentGuid(), r.getReportedUser(), r.getReporterUser(), r.getCategoryCd(), r.getReason(), r.isProcessed(), r.getAuditInfo() == null ? null : r.getAuditInfo().registeredDate())).toList(), result.page(), result.size(), result.totalElements(), result.totalPages(), result.first(), result.last());
    }
}
