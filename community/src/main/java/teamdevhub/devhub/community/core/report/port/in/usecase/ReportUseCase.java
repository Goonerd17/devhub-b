package teamdevhub.devhub.community.core.report.port.in.usecase;

import teamdevhub.devhub.community.core.report.port.in.command.CreateReportCommand;

public interface ReportUseCase {

    void createReport(CreateReportCommand createReportCommand);

    void processReport(String reportGuid);
}
