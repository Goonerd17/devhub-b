package teamdevhub.devhub.admin.http.user.controller;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import teamdevhub.devhub.admin.http.response.*;
import teamdevhub.devhub.admin.http.user.facade.AdminReportHttpFacade;
import teamdevhub.devhub.admin.http.user.model.AdminReportResponseDto;
import teamdevhub.devhub.shared.core.common.page.PageCommand;
import teamdevhub.devhub.web.api.web.model.response.DataApiResponseDto;
import teamdevhub.devhub.web.shared.enums.SuccessCode;
@Tag(name="Admin - Report", description="관리자 신고 관리 API")
@RestController @RequestMapping("/admin/users") @RequiredArgsConstructor
public class AdminReportController {
 private final AdminReportHttpFacade facade;
 @GetMapping("/{userGuid}/reports") public ResponseEntity<DataListApiResponseDto<AdminReportResponseDto>> received(@PathVariable String userGuid,@RequestParam(defaultValue="0") int page,@RequestParam(defaultValue="10") int size){return list(facade.findReceived(userGuid,PageCommand.of(page,size)));}
 @GetMapping("/{userGuid}/reports/reported") public ResponseEntity<DataListApiResponseDto<AdminReportResponseDto>> submitted(@PathVariable String userGuid,@RequestParam(defaultValue="0") int page,@RequestParam(defaultValue="10") int size){return list(facade.findSubmitted(userGuid,PageCommand.of(page,size)));}
 @GetMapping("/reports") public ResponseEntity<DataListApiResponseDto<AdminReportResponseDto>> all(@RequestParam(defaultValue="0") int page,@RequestParam(defaultValue="10") int size){return list(facade.findAll(PageCommand.of(page,size)));}
 @PutMapping("/reports/{reportGuid}/process") public ResponseEntity<DataApiResponseDto<Void>> process(@PathVariable String reportGuid){facade.process(reportGuid);return ResponseEntity.ok(DataApiResponseDto.successWithoutData(SuccessCode.UPDATE_SUCCESS));}
 private ResponseEntity<DataListApiResponseDto<AdminReportResponseDto>> list(teamdevhub.devhub.shared.moderation.ReportPage p){return ResponseEntity.ok(DataListApiResponseDto.successWithDataList(SuccessCode.READ_SUCCESS,facade.toDtos(p),PageResponseDto.from(p)));}
}
