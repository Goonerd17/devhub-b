package teamdevhub.devhub.community.http.report.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import teamdevhub.devhub.community.http.report.model.CreateReportRequestDto;
import teamdevhub.devhub.web.api.model.response.DataApiResponseDto;
import teamdevhub.devhub.community.http.resolver.LoginUser;
import java.lang.String;
import teamdevhub.devhub.community.http.report.facade.ReportFacade;

@Tag(name = "Report", description = "게시글/댓글 신고 API")
@RestController
@RequestMapping("/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportFacade reportFacade;

    @Operation(summary = "게시글/댓글 신고", description = "게시글 또는 댓글을 신고합니다.")
    @ApiResponses({
		@ApiResponse(responseCode = "200", description = "신고 등록 성공"),
		@ApiResponse(responseCode = "401", description = "인증되지 않은 사용자")
    })
    @PostMapping
    public ResponseEntity<DataApiResponseDto<Void>> createReport(
    		@Valid @RequestBody CreateReportRequestDto createReportRequestDto, @LoginUser String authenticatedUser) {
        return ResponseEntity.ok(reportFacade.createReport(createReportRequestDto.toCommand(authenticatedUser)));
    }
}
