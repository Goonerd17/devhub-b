package teamdevhub.devhub.community.api;
import java.util.List;
public record ReportPage(List<ReportView> content, int page, int size, long totalElements, int totalPages, boolean first, boolean last) { }
