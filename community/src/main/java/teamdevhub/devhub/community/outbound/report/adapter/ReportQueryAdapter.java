package teamdevhub.devhub.community.outbound.report.adapter;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import teamdevhub.devhub.platform.core.common.page.PageCommand;
import teamdevhub.devhub.platform.core.common.page.PageResult;
import teamdevhub.devhub.community.core.report.domain.Report;
import teamdevhub.devhub.community.core.report.port.out.ReportQueryRepository;
import teamdevhub.devhub.community.outbound.report.adapter.entity.ReportEntity;
import teamdevhub.devhub.community.outbound.report.adapter.mapper.ReportMapper;
import teamdevhub.devhub.community.outbound.report.persistence.JpaReportRepository;

@Component
@RequiredArgsConstructor
public class ReportQueryAdapter implements ReportQueryRepository {

    private final JpaReportRepository jpaReportRepository;

    @Override
    public PageResult<Report> findByReportedUser(String userGuid, PageCommand pageCommand) {
        Page<ReportEntity> page = jpaReportRepository.findByReportedUser(
                userGuid, toPageable(pageCommand));
        return toPageResult(page);
    }

    @Override
    public PageResult<Report> findByReporterUser(String userGuid, PageCommand pageCommand) {
        Page<ReportEntity> page = jpaReportRepository.findByReporterUser(
                userGuid, toPageable(pageCommand));
        return toPageResult(page);
    }

    @Override
    public PageResult<Report> findAll(PageCommand pageCommand) {
        Page<ReportEntity> page = jpaReportRepository.findAll(toPageable(pageCommand));
        return toPageResult(page);
    }

    private PageRequest toPageable(PageCommand pageCommand) {
        return PageRequest.of(pageCommand.page(), pageCommand.size(),
                Sort.by("registeredDate").descending());
    }

    private PageResult<Report> toPageResult(Page<ReportEntity> page) {
        List<Report> reports = page.getContent().stream()
                .map(ReportMapper::toDomain)
                .toList();
        return PageResult.of(reports, page.getNumber(), page.getSize(), page.getTotalElements());
    }
}
