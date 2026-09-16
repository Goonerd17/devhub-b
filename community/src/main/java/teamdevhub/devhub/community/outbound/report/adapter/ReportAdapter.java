package teamdevhub.devhub.community.outbound.report.adapter;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import teamdevhub.devhub.community.core.report.domain.Report;
import teamdevhub.devhub.community.core.report.port.out.ReportRepository;
import teamdevhub.devhub.community.outbound.report.adapter.mapper.ReportMapper;
import teamdevhub.devhub.community.outbound.report.persistence.JpaReportRepository;

@Component
@RequiredArgsConstructor
public class ReportAdapter implements ReportRepository {

    private final JpaReportRepository jpaReportRepository;

    @Override
    public boolean existsDuplicate(String reporterUser, String boardGuid, String commentGuid) {
        return jpaReportRepository.existsDuplicate(reporterUser, boardGuid, commentGuid);
    }

    @Override
    public void save(Report report) {
        jpaReportRepository.save(ReportMapper.toEntity(report));
    }

    @Override
    public void markProcessed(String reportGuid) {
        jpaReportRepository.markProcessed(reportGuid);
    }
}
