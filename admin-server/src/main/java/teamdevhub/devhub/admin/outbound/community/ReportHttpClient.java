package teamdevhub.devhub.admin.outbound.community;

import org.springframework.stereotype.Component;
import teamdevhub.devhub.admin.core.port.out.ReportPort;
import teamdevhub.devhub.shared.moderation.ReportPage;

@Component
public class ReportHttpClient implements ReportPort {
    private final ReportFeignClient client;

    public ReportHttpClient(ReportFeignClient client) {
        this.client = client;
    }

    public ReportPage findAll(int page, int size) { return client.findAll(page, size); }
    public ReportPage findReceived(String userGuid, int page, int size) { return client.findReceived(userGuid, page, size); }
    public ReportPage findSubmitted(String userGuid, int page, int size) { return client.findSubmitted(userGuid, page, size); }

    public void process(String reportGuid) {
        client.process(reportGuid);
    }
}
