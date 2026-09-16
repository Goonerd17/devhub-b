package teamdevhub.devhub.community.core.report.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import teamdevhub.devhub.community.core.board.domain.Board;
import teamdevhub.devhub.community.core.board.domain.Comment;
import teamdevhub.devhub.community.core.board.port.out.BoardRepository;
import teamdevhub.devhub.community.core.board.port.out.CommentRepository;
import teamdevhub.devhub.platform.core.common.exception.BusinessRuleException;
import teamdevhub.devhub.platform.identifier.IdentifierProvider;
import teamdevhub.devhub.community.core.report.domain.Report;
import teamdevhub.devhub.community.core.report.port.in.command.CreateReportCommand;
import teamdevhub.devhub.community.core.report.port.in.usecase.ReportUseCase;
import teamdevhub.devhub.community.api.ReportProcessing;
import teamdevhub.devhub.community.core.report.port.out.ReportRepository;
import teamdevhub.devhub.platform.shared.enums.ErrorCode;

@Service
@Transactional
@RequiredArgsConstructor
public class ReportService implements ReportUseCase, ReportProcessing {

    private final IdentifierProvider identifierProvider;
    private final ReportRepository reportRepository;
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;

    @Override
    public void createReport(CreateReportCommand createReportCommand) {
        String commentGuid = createReportCommand.commentGuid();
        String reportedUserGuid;

        boolean duplicated = reportRepository.existsDuplicate(createReportCommand.reporterUser(), createReportCommand.boardGuid(), commentGuid);
        if (duplicated) {
            throw BusinessRuleException.of(ErrorCode.REPORT_DUPLICATE);
        }

        if (commentGuid == null) {
            Board board = boardRepository.findByBoardGuid(createReportCommand.boardGuid());
            reportedUserGuid = board.getUserGuid();
        } else {
            Comment comment = commentRepository.findByCommentGuid(commentGuid);
           if (!createReportCommand.boardGuid().equals(comment.getBoardGuid())) {
               throw BusinessRuleException.of(ErrorCode.REPORT_TARGET_MISMATCH);
           }
            reportedUserGuid = comment.getUserGuid();
        }

        String reportGuid = identifierProvider.generateIdentifier();
        Report report = Report.createReport(createReportCommand, reportGuid, reportedUserGuid);
        reportRepository.save(report);
    }

    @Override
    public void processReport(String reportGuid) {
        reportRepository.markProcessed(reportGuid);
    }

    @Override
    public void process(String reportGuid) {
        processReport(reportGuid);
    }
}
