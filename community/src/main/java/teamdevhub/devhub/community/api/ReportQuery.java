package teamdevhub.devhub.community.api;
public interface ReportQuery {
    ReportPage findReceived(String userGuid, int page, int size);
    ReportPage findSubmitted(String userGuid, int page, int size);
    ReportPage findAll(int page, int size);
}
