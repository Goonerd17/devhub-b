package teamdevhub.devhub.web.api.application.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import teamdevhub.devhub.web.api.application.model.request.CreateApplicationRequestDto;
import teamdevhub.devhub.web.api.application.model.response.ProjectApplicationDetailWrapperResponseDto;
import teamdevhub.devhub.web.api.application.model.response.ProjectApplicationListResponseDto;
import teamdevhub.devhub.web.api.web.model.response.DataApiResponseDto;
import teamdevhub.devhub.web.api.web.resolver.LoginUser;
import teamdevhub.devhub.web.api.application.facade.ProjectApplicationFacade;
import teamdevhub.devhub.identity.core.auth.domain.vo.user.AuthenticatedUser;
import teamdevhub.devhub.platform.core.common.page.PageCommand;

@Tag(name = "Application", description = "프로젝트 지원 관리 API")
@RestController
@RequestMapping("/projects")
@RequiredArgsConstructor
public class ApplicationController {

	private final ProjectApplicationFacade projectApplicationFacade;

	@Operation(summary = "지원 승인/거절", description = "프로젝트 지원을 승인하거나 거절합니다. 프로젝트 리더만 처리 가능합니다.")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "처리 성공"),
			@ApiResponse(responseCode = "401", description = "인증되지 않은 사용자"),
			@ApiResponse(responseCode = "403", description = "처리 권한 없음")
	})
	@PutMapping("/applications/{applicationGuid}/approve")
	public ResponseEntity<DataApiResponseDto<Void>> approveApplication(
		@Parameter(description = "지원 GUID", required = true) @PathVariable("applicationGuid") String applicationGuid,
		@Parameter(description = "승인 여부 (true: 승인, false: 거절)", required = true) @RequestParam("approved") boolean approved,
		@LoginUser AuthenticatedUser authenticatedUser
	) {
		return ResponseEntity.ok(
			projectApplicationFacade.approveApplication(applicationGuid, authenticatedUser.userGuid(), approved)
		);
	}

	@Operation(summary = "프로젝트 지원", description = "프로젝트에 지원합니다. 지원서 양식 답변을 포함하여 제출합니다.")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "지원 성공"),
			@ApiResponse(responseCode = "400", description = "유효성 검사 실패"),
			@ApiResponse(responseCode = "401", description = "인증되지 않은 사용자")
	})
	@PostMapping("/{projectGuid}/applications")
	public ResponseEntity<DataApiResponseDto<Void>> createApplication(
		@Parameter(description = "프로젝트 GUID", required = true) @PathVariable("projectGuid") String projectGuid,
		@Valid @RequestBody CreateApplicationRequestDto requestDto,
		@LoginUser AuthenticatedUser authenticatedUser
	) {
		return ResponseEntity.ok(
			projectApplicationFacade.createApplication(projectGuid, authenticatedUser.userGuid(), requestDto)
		);
	}

	@Operation(summary = "프로젝트 지원 취소", description = "본인의 지원을 취소합니다. 아직 승인/거절 처리되지 않은 지원만 취소할 수 있습니다.")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "취소 성공"),
			@ApiResponse(responseCode = "401", description = "인증되지 않은 사용자")
	})
	@PutMapping("/applications/{applicationGuid}/cancel")
	public ResponseEntity<DataApiResponseDto<Void>> cancelApplication(
		@Parameter(description = "지원 GUID", required = true) @PathVariable("applicationGuid") String applicationGuid,
		@LoginUser AuthenticatedUser authenticatedUser
	) {
		return ResponseEntity.ok(
			projectApplicationFacade.cancelApplication(applicationGuid, authenticatedUser.userGuid())
		);
	}

	@Operation(summary = "프로젝트 지원 목록 조회", description = "프로젝트에 대한 지원 목록을 페이징 조회합니다.")
	@ApiResponse(responseCode = "200", description = "조회 성공")
	@GetMapping("/{projectGuid}/applications")
	public ResponseEntity<DataApiResponseDto<ProjectApplicationListResponseDto>> getApplicationsByProjectGuid(
		@Parameter(description = "프로젝트 GUID", required = true) @PathVariable("projectGuid") String projectGuid,
		@Parameter(description = "페이지 번호", example = "0") @RequestParam("page") int page,
		@Parameter(description = "페이지 크기", example = "10") @RequestParam("size") int size
	) {
		return ResponseEntity.ok(
			projectApplicationFacade.getApplicationsByProjectGuid(projectGuid, PageCommand.of(page, size))
		);
	}

	@Operation(summary = "지원 상세 조회", description = "지원 GUID로 지원서 상세 내용(답변 포함)을 조회합니다.")
	@ApiResponse(responseCode = "200", description = "조회 성공")
	@GetMapping("/applications/{applicationGuid}")
	public ResponseEntity<DataApiResponseDto<ProjectApplicationDetailWrapperResponseDto>> getApplicationDetail(
		@Parameter(description = "지원 GUID", required = true) @PathVariable("applicationGuid") String applicationGuid
	) {
		return ResponseEntity.ok(
			projectApplicationFacade.getApplicationDetail(applicationGuid)
		);
	}
}
