package teamdevhub.devhub.project.http.application.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import teamdevhub.devhub.project.http.application.facade.AdminProjectApplicationFacade;
import teamdevhub.devhub.project.http.application.model.request.SearchAdminProjectApplicationRequestDto;
import teamdevhub.devhub.project.http.application.model.response.AdminProjectApplicationListResponseDto;
import teamdevhub.devhub.shared.core.common.page.PageCommand;
import teamdevhub.devhub.web.api.model.response.DataApiResponseDto;

@Tag(name = "Admin - Application", description = "관리자 지원서 조회 API")
@RestController
@RequestMapping("/projects")
@RequiredArgsConstructor
public class AdminApplicationQueryController {

    private final AdminProjectApplicationFacade adminProjectApplicationFacade;

    @Operation(summary = "프로젝트 지원자 목록 조회")
    @GetMapping("/{projectGuid}/applicants")
    public ResponseEntity<DataApiResponseDto<AdminProjectApplicationListResponseDto>> getApplications(
            @Parameter(description = "프로젝트 GUID") @PathVariable String projectGuid,
            @ModelAttribute SearchAdminProjectApplicationRequestDto request,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(adminProjectApplicationFacade.getApplicationsByProjectGuid(
                request.toCommand(projectGuid), PageCommand.of(page, size)));
    }
}
