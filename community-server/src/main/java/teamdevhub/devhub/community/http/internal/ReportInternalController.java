package teamdevhub.devhub.community.http.internal;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import teamdevhub.devhub.community.api.ReportProcessing;
import teamdevhub.devhub.community.api.ReportQuery;
import teamdevhub.devhub.shared.moderation.ReportPage;
import teamdevhub.devhub.shared.moderation.ReportView;

@RestController
@RequestMapping("/internal/reports")
@RequiredArgsConstructor
public class ReportInternalController {
    private final ReportQuery reportQuery;
    private final ReportProcessing reportProcessing;

    @GetMapping
    public ReportPage all(@RequestParam int page, @RequestParam int size) {
        return page(reportQuery.findAll(page, size));
    }

    @GetMapping("/received/{userGuid}")
    public ReportPage received(@PathVariable String userGuid, @RequestParam int page, @RequestParam int size) {
        return page(reportQuery.findReceived(userGuid, page, size));
    }

    @GetMapping("/submitted/{userGuid}")
    public ReportPage submitted(@PathVariable String userGuid, @RequestParam int page, @RequestParam int size) {
        return page(reportQuery.findSubmitted(userGuid, page, size));
    }

    @PutMapping("/{reportGuid}/process")
    public void process(@PathVariable String reportGuid) { reportProcessing.process(reportGuid); }

    private ReportPage page(teamdevhub.devhub.community.api.ReportPage source) {
        return new ReportPage(source.content().stream().map(report -> new ReportView(report.reportGuid(),
                report.boardGuid(), report.commentGuid(), report.reportedUser(), report.reporterUser(),
                report.categoryCd(), report.reason(), report.processed(), report.registeredDate())).toList(),
                source.page(), source.size(), source.totalElements(), source.totalPages(), source.first(), source.last());
    }
}
