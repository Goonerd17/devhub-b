package teamdevhub.devhub.admin.http.user.facade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import teamdevhub.devhub.admin.http.user.model.AdminReportResponseDto;
import teamdevhub.devhub.shared.moderation.ReportGateway;
import teamdevhub.devhub.shared.moderation.ReportPage;
import teamdevhub.devhub.shared.core.common.page.PageCommand;
@Service @RequiredArgsConstructor
public class AdminReportHttpFacade {
    private final ReportGateway reportQuery;
    public ReportPage findAll(PageCommand p){return reportQuery.findAll(p.page(),p.size());}
    public ReportPage findReceived(String g,PageCommand p){return reportQuery.findReceived(g,p.page(),p.size());}
    public ReportPage findSubmitted(String g,PageCommand p){return reportQuery.findSubmitted(g,p.page(),p.size());}
    public void process(String g){reportQuery.process(g);}
    public java.util.List<AdminReportResponseDto> toDtos(ReportPage p){return p.content().stream().map(AdminReportResponseDto::fromView).toList();}
}
