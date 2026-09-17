package teamdevhub.devhub.community.core.report.domain;

import lombok.Builder;
import lombok.Getter;
import teamdevhub.devhub.shared.core.common.audit.AuditInfo;
import teamdevhub.devhub.community.core.report.port.in.command.CreateReportCommand;

@Getter
@Builder
public class Report {

    private final String reportGuid;
    private final String boardGuid;
    private final String commentGuid;
    private final String reportedUser;
    private final String reporterUser;
    private final String categoryCd;
    private final String reason;
    private final boolean isProcessed;
    private final AuditInfo auditInfo;

    public static Report of(
            String reportGuid,
            String boardGuid,
            String commentGuid,
            String reportedUser,
            String reporterUser,
            String categoryCd,
            String reason,
            boolean isProcessed,
            AuditInfo auditInfo
    ) {
        return Report.builder()
                .reportGuid(reportGuid)
                .boardGuid(boardGuid)
                .commentGuid(commentGuid)
                .reportedUser(reportedUser)
                .reporterUser(reporterUser)
                .categoryCd(categoryCd)
                .reason(reason)
                .isProcessed(isProcessed)
                .auditInfo(auditInfo)
                .build();
    }

    public static Report createReport(CreateReportCommand createReportCommand, String reportGuid, String reportedUserGuid) {
        return Report.builder()
                .reportGuid(reportGuid)
                .boardGuid(createReportCommand.boardGuid())
                .commentGuid(createReportCommand.commentGuid())
                .reportedUser(reportedUserGuid)
                .reporterUser(createReportCommand.reporterUser())
                .categoryCd(createReportCommand.categoryCd())
                .reason(createReportCommand.reason())
                .isProcessed(false)	//enum 처리가 나을지도
                .build();
    }
}
