package teamdevhub.devhub.community.core.report.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import teamdevhub.devhub.platform.core.common.audit.AuditInfo;
import teamdevhub.devhub.platform.core.common.page.PageCommand;
import teamdevhub.devhub.platform.core.common.page.PageResult;
import teamdevhub.devhub.community.core.report.application.service.ReportQueryService;
import teamdevhub.devhub.community.core.report.domain.Report;
import teamdevhub.devhub.fake.pure.application.port.out.report.FakeReportQueryRepository;

import static org.assertj.core.api.Assertions.assertThat;

class ReportQueryServiceTest {

    private static final String TEST_BOARD_GUID_1 = "BOARD1a1b2c3d4e5f6g7h8i9j10k11l12";
    private static final String TEST_REPORT_GUID_1 = "REPORT1a1b2c3d4e5f6g7h8i9j10k11l";
    private static final String TEST_USER_GUID_1 = "USER1a1b2c3d4e5f6g7h8i9j10k11l12";
    private static final String TEST_USER_GUID_2 = "USER2a1b2c3d4e5f6g7h8i9j10k11l12";

    private ReportQueryService reportQueryService;
    private FakeReportQueryRepository reportQueryRepository;

    @BeforeEach
    void init() {
        reportQueryRepository = new FakeReportQueryRepository();
        reportQueryService = new ReportQueryService(reportQueryRepository);
    }

    @Test
    @DisplayName("피신고자로_조회하면_해당_사용자가_신고받은_내역만_반환된다")
    void getReportsByReportedUser_returnsOnlyMatchingReports() {
        // given
        Report report1 = Report.of(TEST_REPORT_GUID_1, TEST_BOARD_GUID_1, null,
                TEST_USER_GUID_1, TEST_USER_GUID_2, "SPAM", "도배", false, AuditInfo.empty());
        Report report2 = Report.of("RPT2", TEST_BOARD_GUID_1, null,
                TEST_USER_GUID_2, TEST_USER_GUID_1, "ABUSE", "욕설", false, AuditInfo.empty());
        reportQueryRepository.givenReport(report1);
        reportQueryRepository.givenReport(report2);

        // when
        PageResult<Report> result = reportQueryService.getReportsByReportedUser(
                TEST_USER_GUID_1, PageCommand.of(0, 10));

        // then
        assertThat(result.content()).hasSize(1);
        assertThat(result.content().get(0).getReportedUser()).isEqualTo(TEST_USER_GUID_1);
    }

    @Test
    @DisplayName("신고자로_조회하면_해당_사용자가_신고한_내역만_반환된다")
    void getReportsByReporterUser_returnsOnlyMatchingReports() {
        // given
        Report report1 = Report.of(TEST_REPORT_GUID_1, TEST_BOARD_GUID_1, null,
                TEST_USER_GUID_2, TEST_USER_GUID_1, "SPAM", "도배", false, AuditInfo.empty());
        Report report2 = Report.of("RPT2", TEST_BOARD_GUID_1, null,
                TEST_USER_GUID_1, TEST_USER_GUID_2, "ABUSE", "욕설", false, AuditInfo.empty());
        reportQueryRepository.givenReport(report1);
        reportQueryRepository.givenReport(report2);

        // when
        PageResult<Report> result = reportQueryService.getReportsByReporterUser(
                TEST_USER_GUID_1, PageCommand.of(0, 10));

        // then
        assertThat(result.content()).hasSize(1);
        assertThat(result.content().get(0).getReporterUser()).isEqualTo(TEST_USER_GUID_1);
    }

    @Test
    @DisplayName("전체_신고_목록을_조회하면_저장된_모든_신고가_반환된다")
    void getAllReports_returnsAllReports() {
        // given
        Report report1 = Report.of(TEST_REPORT_GUID_1, TEST_BOARD_GUID_1, null,
                TEST_USER_GUID_1, TEST_USER_GUID_2, "SPAM", "도배", false, AuditInfo.empty());
        Report report2 = Report.of("RPT2", TEST_BOARD_GUID_1, null,
                TEST_USER_GUID_2, TEST_USER_GUID_1, "ABUSE", "욕설", false, AuditInfo.empty());
        reportQueryRepository.givenReport(report1);
        reportQueryRepository.givenReport(report2);

        // when
        PageResult<Report> result = reportQueryService.getAllReports(PageCommand.of(0, 10));

        // then
        assertThat(result.content()).hasSize(2);
        assertThat(result.totalElements()).isEqualTo(2);
    }

    @Test
    @DisplayName("신고_내역이_없을_때_조회하면_빈_목록이_반환된다")
    void getReportsByReportedUser_noReports_returnsEmpty() {
        // when
        PageResult<Report> result = reportQueryService.getReportsByReportedUser(
                TEST_USER_GUID_1, PageCommand.of(0, 10));

        // then
        assertThat(result.content()).isEmpty();
        assertThat(result.totalElements()).isZero();
    }
}
