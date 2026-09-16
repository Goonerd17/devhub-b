package teamdevhub.devhub.web.api.admin.form.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import teamdevhub.devhub.web.api.admin.form.model.SaveApplicationFormRequestDto;
import teamdevhub.devhub.web.api.admin.form.model.SearchAdminFormRequestDto;
import teamdevhub.devhub.web.api.web.model.response.DataApiResponseDto;
import teamdevhub.devhub.web.api.web.model.response.DataListApiResponseDto;
import teamdevhub.devhub.web.api.admin.form.facade.ApplicationFormFacade;
import teamdevhub.devhub.web.api.admin.form.facade.ApplicationFormFacade.ApplicationFormListItemDto;

@Tag(name = "Admin - Form", description = "관리자 지원 폼 관리 API")
@RestController("adminApplicationFormController")
@RequestMapping("/admin/form")
@RequiredArgsConstructor
public class ApplicationFormController {

	private final ApplicationFormFacade adminApplicationFormFacade;

	@Operation(summary = "지원 폼 목록 조회", description = "제목으로 검색하여 지원 폼 목록을 조회합니다.")
	@ApiResponse(responseCode = "200", description = "조회 성공")
	@GetMapping("/list")
	public ResponseEntity<DataListApiResponseDto<ApplicationFormListItemDto>> getApplicationForms(
			@ModelAttribute SearchAdminFormRequestDto searchDto) {
		return ResponseEntity.ok(
				adminApplicationFormFacade.getApplicationForms(searchDto.toCommand())
		);
	}

	@Operation(summary = "지원 폼 저장", description = "지원 폼을 등록하거나 수정합니다. GUID가 없으면 등록, 있으면 수정합니다.")
	@ApiResponse(responseCode = "200", description = "저장 성공")
	@PutMapping
	public ResponseEntity<DataApiResponseDto<Void>> saveApplicationForm(
			@Valid @RequestBody SaveApplicationFormRequestDto requestDto) {
		return ResponseEntity.ok(
				adminApplicationFormFacade.saveApplicationForm(requestDto.toCommand())
		);
	}

	@Operation(summary = "지원 폼 삭제", description = "지원 폼을 삭제합니다.")
	@ApiResponse(responseCode = "200", description = "삭제 성공")
	@DeleteMapping("/{applicationFormGuid}")
	public ResponseEntity<DataApiResponseDto<Void>> deleteApplicationForm(
			@Parameter(description = "applicationFormGuid", required = true) @PathVariable String applicationFormGuid) {
		return ResponseEntity.ok(
				adminApplicationFormFacade.deleteApplicationForm (applicationFormGuid)
		);
	}
}
