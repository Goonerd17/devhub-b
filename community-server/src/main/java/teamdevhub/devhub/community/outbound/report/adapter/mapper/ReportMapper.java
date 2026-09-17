package teamdevhub.devhub.community.outbound.report.adapter.mapper;

import teamdevhub.devhub.shared.core.common.audit.AuditInfo;
import teamdevhub.devhub.community.core.report.domain.Report;
import teamdevhub.devhub.community.outbound.report.adapter.entity.ReportEntity;

public class ReportMapper {

    private ReportMapper() {}

    public static Report toDomain(ReportEntity entity) {
        return Report.of(
                entity.getReportGuid(),
                entity.getBoardGuid(),
                entity.getCommentGuid(),
                entity.getReportedUser(),
                entity.getReporterUser(),
                entity.getCategoryCd(),
                entity.getReason(),
                entity.isProcessed(),
                AuditInfo.of(
                        entity.getRegistrantGuid(),
                        entity.getRegisteredDate(),
                        entity.getModifierGuid(),
                        entity.getModifiedDate()
                )
        );
    }

    public static ReportEntity toEntity(Report report) {
        return ReportEntity.builder()
                .reportGuid(report.getReportGuid())
                .boardGuid(report.getBoardGuid())
                .commentGuid(report.getCommentGuid())
                .reportedUser(report.getReportedUser())
                .reporterUser(report.getReporterUser())
                .categoryCd(report.getCategoryCd())
                .reason(report.getReason())
                .isProcessed(report.isProcessed())
                .build();
    }
}
