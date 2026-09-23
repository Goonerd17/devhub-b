package teamdevhub.devhub.admin.core.port.out;

import teamdevhub.devhub.shared.moderation.ReportPage;

public interface ReportPort {
    ReportPage findReceived(String userGuid, int page, int size);
    ReportPage findSubmitted(String userGuid, int page, int size);
    ReportPage findAll(int page, int size);
    void process(String reportGuid);
}
