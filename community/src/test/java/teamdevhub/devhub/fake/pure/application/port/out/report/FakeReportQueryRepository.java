package teamdevhub.devhub.fake.pure.application.port.out.report;

import teamdevhub.devhub.platform.core.common.page.PageCommand;
import teamdevhub.devhub.platform.core.common.page.PageResult;
import teamdevhub.devhub.community.core.report.domain.Report;
import teamdevhub.devhub.community.core.report.port.out.ReportQueryRepository;

import java.util.ArrayList;
import java.util.List;

public class FakeReportQueryRepository implements ReportQueryRepository {

    private final List<Report> store = new ArrayList<>();

    public void givenReport(Report report) {
        store.add(report);
    }

    @Override
    public PageResult<Report> findByReportedUser(String userGuid, PageCommand pageCommand) {
        List<Report> filtered = store.stream()
                .filter(r -> userGuid.equals(r.getReportedUser()))
                .toList();
        return PageResult.of(filtered, pageCommand.page(), pageCommand.size(), filtered.size());
    }

    @Override
    public PageResult<Report> findByReporterUser(String userGuid, PageCommand pageCommand) {
        List<Report> filtered = store.stream()
                .filter(r -> userGuid.equals(r.getReporterUser()))
                .toList();
        return PageResult.of(filtered, pageCommand.page(), pageCommand.size(), filtered.size());
    }

    @Override
    public PageResult<Report> findAll(PageCommand pageCommand) {
        return PageResult.of(store, pageCommand.page(), pageCommand.size(), store.size());
    }
}
