package teamdevhub.devhub.project.http.application.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import teamdevhub.devhub.project.http.application.facade.ProjectApplicationFacade;
import teamdevhub.devhub.project.http.application.model.response.ProjectApplicationDetailWrapperResponseDto;
import teamdevhub.devhub.project.http.application.model.response.ProjectApplicationListResponseDto;
import teamdevhub.devhub.web.api.model.response.DataApiResponseDto;
import teamdevhub.devhub.shared.core.common.page.PageCommand;

@Tag(name = "Application", description = "프로젝트 지원 조회 API")
@RestController
@RequestMapping("/projects")
@RequiredArgsConstructor
public class ApplicationQueryController {

    private final ProjectApplicationFacade projectApplicationFacade;

    @Operation(summary = "프로젝트 지원 목록 조회")
    @GetMapping("/{projectGuid}/applications")
    public ResponseEntity<DataApiResponseDto<ProjectApplicationListResponseDto>> getApplications(
            @Parameter(description = "프로젝트 GUID") @PathVariable String projectGuid,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(projectApplicationFacade.getApplicationsByProjectGuid(projectGuid, PageCommand.of(page, size)));
    }

    @Operation(summary = "지원 상세 조회")
    @GetMapping("/applications/{applicationGuid}")
    public ResponseEntity<DataApiResponseDto<ProjectApplicationDetailWrapperResponseDto>> getApplicationDetail(
            @Parameter(description = "지원 GUID") @PathVariable String applicationGuid) {
        return ResponseEntity.ok(projectApplicationFacade.getApplicationDetail(applicationGuid));
    }
}
