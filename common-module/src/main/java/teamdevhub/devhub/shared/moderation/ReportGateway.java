package teamdevhub.devhub.shared.moderation;

public interface ReportGateway {
    ReportPage findReceived(String userGuid, int page, int size);
    ReportPage findSubmitted(String userGuid, int page, int size);
    ReportPage findAll(int page, int size);
    void process(String reportGuid);
}
