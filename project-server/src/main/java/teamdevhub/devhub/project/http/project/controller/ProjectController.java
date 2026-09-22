package teamdevhub.devhub.project.http.project.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import teamdevhub.devhub.project.http.project.model.CreateProjectRequestDto;
import teamdevhub.devhub.project.http.project.model.SearchProjectRequestDto;
import teamdevhub.devhub.project.http.project.model.UpdateProjectRequestDto;
import teamdevhub.devhub.web.api.model.response.DataApiResponseDto;
import teamdevhub.devhub.project.http.response.DataListApiResponseDto;
import teamdevhub.devhub.project.http.response.PageResponseDto;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import teamdevhub.devhub.shared.security.CurrentUserPrincipal;
import teamdevhub.devhub.shared.core.common.page.PageCommand;
import teamdevhub.devhub.shared.core.common.page.PageResult;
import teamdevhub.devhub.project.core.project.domain.vo.command.CreateProjectLikeCommand;
import teamdevhub.devhub.project.http.project.facade.ProjectFacade;
import teamdevhub.devhub.project.http.project.model.ProjectDetailResponseDto;
import teamdevhub.devhub.project.http.project.model.ProjectDetailWithFormResponseDto;
import teamdevhub.devhub.web.enums.SuccessCode;

@Tag(name = "Project", description = "프로젝트 생성/조회/수정/삭제 API")
@RestController
@RequestMapping("/projects")
@RequiredArgsConstructor
public class ProjectController {

	private final ProjectFacade projectFacade;

	@Operation(summary = "프로젝트 생성", description = "새로운 프로젝트를 생성합니다.")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "프로젝트 생성 성공"),
			@ApiResponse(responseCode = "400", description = "유효성 검사 실패"),
			@ApiResponse(responseCode = "401", description = "인증되지 않은 사용자")
	})
	@PostMapping
	public ResponseEntity<DataApiResponseDto<Void>> createProject(@Valid @RequestBody CreateProjectRequestDto createProjectRequestDto, @AuthenticationPrincipal CurrentUserPrincipal authenticatedUser) {
		projectFacade.createProject(createProjectRequestDto.toCommand(authenticatedUser.userGuid()));
        return ResponseEntity.ok(DataApiResponseDto.successWithoutData(SuccessCode.CREATE_SUCCESS));
    }

	@Operation(summary = "프로젝트 목록 조회", description = "검색 조건(제목, 포지션, 스킬 등)으로 프로젝트 목록을 페이징 조회합니다. 로그인 시 좋아요 여부가 포함됩니다.")
	@ApiResponse(responseCode = "200", description = "조회 성공")
	@GetMapping
    public ResponseEntity<DataListApiResponseDto<ProjectDetailResponseDto>> getProjectList(
    		@Valid @ModelAttribute SearchProjectRequestDto searchProjectRequestDto,
    		@Parameter(description = "페이지 번호 (0부터 시작)", example = "0") @RequestParam("page") int page,
    		@Parameter(description = "페이지 크기", example = "10") @RequestParam("size") int size,
    		@AuthenticationPrincipal CurrentUserPrincipal user) {
		PageResult<ProjectDetailResponseDto> pagedProjectDetail = projectFacade.getProjectList(searchProjectRequestDto.toSearchProjectListCommand(), PageCommand.of(page, size), user);
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
			@AuthenticationPrincipal CurrentUserPrincipal user) {
		ProjectDetailResponseDto responseDto = projectFacade.getProjectDetail(projectGuid, user);
		return ResponseEntity.ok(
			DataApiResponseDto.successWithData(SuccessCode.READ_SUCCESS, responseDto)
		);
	
	}
	
	@Operation(summary = "프로젝트 + 지원서 양식 조회", description = "프로젝트 상세 정보와 지원서 양식을 함께 조회합니다.")
	@ApiResponse(responseCode = "200", description = "조회 성공")
	@GetMapping("/{projectGuid}/form")
	public ResponseEntity<DataApiResponseDto<ProjectDetailWithFormResponseDto>> getProjectDetailWithForm(
			@Parameter(description = "프로젝트 GUID", required = true) @PathVariable("projectGuid") String projectGuid) {
		ProjectDetailWithFormResponseDto responseDto = projectFacade.getProjectDetailWithForm(projectGuid);
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
			@Valid @RequestBody UpdateProjectRequestDto updateProjectRequestDto,
			@AuthenticationPrincipal CurrentUserPrincipal authenticatedUser) {
		projectFacade.updateProject(projectGuid, updateProjectRequestDto.toCommand(), authenticatedUser);
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
			@AuthenticationPrincipal CurrentUserPrincipal authenticatedUser) {
		projectFacade.deleteProject(projectGuid, authenticatedUser);
		return ResponseEntity.ok(
				DataApiResponseDto.successWithoutData(SuccessCode.DELETE_SUCCESS)
		);
	}
	
	@Operation(summary = "프로젝트 좋아요 토글", description = "프로젝트에 좋아요를 추가하거나 취소합니다.")
	@ApiResponse(responseCode = "200", description = "좋아요 토글 성공")
	@PostMapping("/{projectGuid}/likes")
	public ResponseEntity<DataApiResponseDto<Void>> toggleProjectLike(
			@Parameter(description = "프로젝트 GUID", required = true) @PathVariable("projectGuid") String projectGuid,
			@AuthenticationPrincipal CurrentUserPrincipal authenticatedUser) {
		projectFacade.toggleProjectLike(CreateProjectLikeCommand.toCreateProjectLikeCommand(projectGuid, authenticatedUser.userGuid()));
		return ResponseEntity.ok(DataApiResponseDto.successWithoutData(SuccessCode.CREATE_SUCCESS));
	}
	
	@PostMapping("/{projectGuid}/close")
	public ResponseEntity<DataApiResponseDto<Void>> closeProject(@PathVariable("projectGuid") String projectGuid, @AuthenticationPrincipal CurrentUserPrincipal authenticatedUser) {
		projectFacade.closeProject(projectGuid, authenticatedUser);
		return ResponseEntity.ok(DataApiResponseDto.successWithoutData(SuccessCode.CREATE_SUCCESS));
	}
}
