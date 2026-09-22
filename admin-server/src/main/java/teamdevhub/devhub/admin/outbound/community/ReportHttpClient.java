package teamdevhub.devhub.admin.outbound.community;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import teamdevhub.devhub.shared.moderation.ReportGateway;
import teamdevhub.devhub.shared.moderation.ReportPage;

@Component
public class ReportHttpClient implements ReportGateway {
    private final RestClient client;

    public ReportHttpClient(RestClient.Builder builder,
            @Value("${services.community.base-url:http://community-server}") String baseUrl) {
        this.client = builder.baseUrl(baseUrl).build();
    }

    public ReportPage findAll(int page, int size) { return get("/internal/reports", null, page, size); }
    public ReportPage findReceived(String userGuid, int page, int size) { return get("/internal/reports/received", userGuid, page, size); }
    public ReportPage findSubmitted(String userGuid, int page, int size) { return get("/internal/reports/submitted", userGuid, page, size); }

    public void process(String reportGuid) {
        client.put().uri("/internal/reports/{reportGuid}/process", reportGuid).retrieve().toBodilessEntity();
    }

    private ReportPage get(String path, String userGuid, int page, int size) {
        return client.get().uri(uri -> {
            var builder = uri.path(path);
            if (userGuid != null) builder.pathSegment(userGuid);
            return builder.queryParam("page", page).queryParam("size", size).build();
        }).retrieve().body(ReportPage.class);
    }
}
