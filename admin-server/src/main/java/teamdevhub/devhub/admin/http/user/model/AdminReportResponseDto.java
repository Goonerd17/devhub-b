package teamdevhub.devhub.admin.http.user.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import teamdevhub.devhub.shared.moderation.ReportView;
import java.time.LocalDateTime;

@Schema(description = "신고 정보 응답")
@Getter @Builder
public class AdminReportResponseDto {
    private String reportGuid; private String boardGuid; private String commentGuid;
    private String reportedUser; private String reporterUser; private String categoryCd; private String reason;
    private boolean processed; private LocalDateTime registeredDate;
    public static AdminReportResponseDto fromView(ReportView report) { return builder().reportGuid(report.reportGuid()).boardGuid(report.boardGuid()).commentGuid(report.commentGuid()).reportedUser(report.reportedUser()).reporterUser(report.reporterUser()).categoryCd(report.categoryCd()).reason(report.reason()).processed(report.processed()).registeredDate(report.registeredDate()).build(); }
}
