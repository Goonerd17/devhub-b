package teamdevhub.devhub.community.api;
import java.time.LocalDateTime;
public record ReportView(String reportGuid, String boardGuid, String commentGuid, String reportedUser, String reporterUser, String categoryCd, String reason, boolean processed, LocalDateTime registeredDate) { }
