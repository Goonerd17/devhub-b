package teamdevhub.devhub.web.api.project.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import teamdevhub.devhub.web.api.application.model.request.SearchAdminProjectApplicationRequestDto;
import teamdevhub.devhub.web.api.application.model.response.AdminProjectApplicationListResponseDto;
import teamdevhub.devhub.web.api.project.model.AdminSearchProjectRequestDto;
import teamdevhub.devhub.web.api.project.model.AdminUpdateProjectRequestDto;
import teamdevhub.devhub.web.api.web.model.response.DataApiResponseDto;
import teamdevhub.devhub.web.api.web.model.response.DataListApiResponseDto;
import teamdevhub.devhub.web.api.web.model.response.PageResponseDto;
import teamdevhub.devhub.web.api.web.resolver.LoginUser;
import teamdevhub.devhub.web.api.project.facade.AdminProjectApplicationFacade;
import teamdevhub.devhub.identity.core.auth.domain.vo.user.AuthenticatedUser;
import teamdevhub.devhub.platform.core.common.page.PageCommand;
import teamdevhub.devhub.platform.core.common.page.PageResult;
import teamdevhub.devhub.web.core.project.port.in.facade.AdminProjectFacade;
import teamdevhub.devhub.web.core.project.port.in.facade.model.ProjectDetailResponseDto;
import teamdevhub.devhub.web.shared.enums.SuccessCode;

@Tag(name = "Admin - Project", description = "어드민 프로젝트 생성/조회/수정/삭제 API")
@RestController
@RequestMapping("/admin/projects")
@RequiredArgsConstructor
public class AdminProjectController {
	
	private final AdminProjectFacade adminProjectFacade;
	private final AdminProjectApplicationFacade adminProjectApplicationFacade;

	@Operation(summary = "프로젝트 목록 조회", description = "검색 조건(제목, 포지션, 스킬 등)으로 프로젝트 목록을 페이징 조회합니다. 로그인 시 좋아요 여부가 포함됩니다.")
	@ApiResponse(responseCode = "200", description = "조회 성공")
	@GetMapping
    public ResponseEntity<DataListApiResponseDto<ProjectDetailResponseDto>> getProjectList(
    		@Valid @ModelAttribute AdminSearchProjectRequestDto adminSearchProjectRequestDto,
    		@Parameter(description = "페이지 번호 (0부터 시작)", example = "0") @RequestParam("page") int page,
    		@Parameter(description = "페이지 크기", example = "10") @RequestParam("size") int size,
    		@AuthenticationPrincipal AuthenticatedUser user) {
		PageResult<ProjectDetailResponseDto> pagedProjectDetail = adminProjectFacade.getProjectList(adminSearchProjectRequestDto.toAdminSearchProjectRequestCommand(), PageCommand.of(page, size), user);
		return ResponseEntity.ok(
				DataListApiResponseDto.successWithDataList(
		                SuccessCode.READ_SUCCESS,
		                pagedProjectDetail.content(),
		                PageResponseDto.from(pagedProjectDetail)
				)
		);
    }
	
	@Operation(summary = "프로젝트 상세 조회", description = "프로젝트 GUID로 프로젝트 상세 정보를 조회합니다.")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "조회 성공"),
			@ApiResponse(responseCode = "404", description = "프로젝트를 찾을 수 없음")
	})
	@GetMapping("/{projectGuid}")
	public ResponseEntity<DataApiResponseDto<ProjectDetailResponseDto>> getProjectDetail(
			@Parameter(description = "프로젝트 GUID", required = true) @PathVariable("projectGuid") String projectGuid,
			@AuthenticationPrincipal AuthenticatedUser user) {
		ProjectDetailResponseDto responseDto = adminProjectFacade.getProjectDetail(projectGuid, user);
		return ResponseEntity.ok(
			DataApiResponseDto.successWithData(SuccessCode.READ_SUCCESS, responseDto)
		);
	
	}
	
	@Operation(summary = "프로젝트 수정", description = "프로젝트 정보를 수정합니다. 프로젝트 생성자만 수정 가능합니다.")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "수정 성공"),
			@ApiResponse(responseCode = "401", description = "인증되지 않은 사용자"),
			@ApiResponse(responseCode = "403", description = "수정 권한 없음")
	})
	@PutMapping("/{projectGuid}")
	public ResponseEntity<DataApiResponseDto<Void>> updateProject(
			@Parameter(description = "프로젝트 GUID", required = true) @PathVariable("projectGuid") String projectGuid,
			@Valid @RequestBody AdminUpdateProjectRequestDto adminUpdateProjectRequestDto,
			@LoginUser AuthenticatedUser authenticatedUser) {
		adminProjectFacade.updateProject(projectGuid, adminUpdateProjectRequestDto.toCommand());
		return ResponseEntity.ok(
			DataApiResponseDto.successWithoutData(SuccessCode.UPDATE_SUCCESS)
		);
	
	}
	
	@Operation(summary = "프로젝트 삭제", description = "프로젝트를 삭제합니다.")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "삭제 성공"),
			@ApiResponse(responseCode = "401", description = "인증되지 않은 사용자"),
			@ApiResponse(responseCode = "404", description = "프로젝트를 찾을 수 없음")
	})
	@DeleteMapping("/{projectGuid}")
	public ResponseEntity<DataApiResponseDto<Void>> deleteProject(
			@Parameter(description = "프로젝트 GUID", required = true) @PathVariable("projectGuid") String projectGuid,
			@LoginUser AuthenticatedUser authenticatedUser) {
		adminProjectFacade.deleteProject(projectGuid, authenticatedUser);
		return ResponseEntity.ok(
				DataApiResponseDto.successWithoutData(SuccessCode.DELETE_SUCCESS)
		);
	}
	
	@Operation(summary = "프로젝트 지원 목록 조회", description = "프로젝트에 대한 지원 목록을 페이징 조회합니다.")
	@ApiResponse(responseCode = "200", description = "조회 성공")
	@GetMapping("/{projectGuid}/applicants")
	public ResponseEntity<DataApiResponseDto<AdminProjectApplicationListResponseDto>> getApplicationsByProjectGuid(
		@Parameter(description = "프로젝트 GUID", required = true) @PathVariable("projectGuid") String projectGuid,
		@Parameter(description = "페이지 번호", example = "0") @RequestParam("page") int page,
		@Parameter(description = "페이지 크기", example = "10") @RequestParam("size") int size,
		@ModelAttribute SearchAdminProjectApplicationRequestDto searchAdminProjectApplicationRequestDto
	) {
		return ResponseEntity.ok(
			adminProjectApplicationFacade.getApplicationsByProjectGuid(searchAdminProjectApplicationRequestDto.toCommand(projectGuid), PageCommand.of(page, size))
		);
	}

	@Operation(summary = "지원자 승인/거절", description = "프로젝트 지원자를 승인하거나 거절합니다.")
	@ApiResponse(responseCode = "200", description = "처리 성공")
	@PutMapping("/{projectGuid}/applicants/{applicationGuid}/status")
	public ResponseEntity<DataApiResponseDto<Void>> approveApplicant(
		@Parameter(description = "프로젝트 GUID", required = true) @PathVariable("projectGuid") String projectGuid,
		@Parameter(description = "지원 GUID", required = true) @PathVariable("applicationGuid") String applicationGuid,
		@Parameter(description = "승인 여부 (true: 승인, false: 거절)", required = true) @RequestParam("approved") boolean approved,
		@LoginUser AuthenticatedUser authenticatedUser
	) {
		return ResponseEntity.ok(
			adminProjectApplicationFacade.approveApplication(applicationGuid, authenticatedUser.userGuid(), approved)
		);
	}
}
