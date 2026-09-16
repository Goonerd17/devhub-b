package teamdevhub.devhub.web.api.application.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import teamdevhub.devhub.web.api.application.model.request.SearchApplicationFormRequestDto;
import teamdevhub.devhub.web.api.web.model.response.DataListApiResponseDto;
import teamdevhub.devhub.web.api.application.facade.ApplicationFormFacade;
import teamdevhub.devhub.web.core.application.port.in.facade.model.ApplicationFormResponseDto;

@Tag(name = "Application", description = "프로젝트 지원 관리 API")
@RestController
@RequestMapping("/applicationForms")
@RequiredArgsConstructor
public class ApplicationFormController {

	private final ApplicationFormFacade applicationApplicationFormFacade;

	@Operation(summary = "지원서 양식 목록 조회", description = "검색 조건으로 지원서 양식 목록을 조회합니다.")
	@ApiResponse(responseCode = "200", description = "조회 성공")
	@GetMapping
	public ResponseEntity<DataListApiResponseDto<ApplicationFormResponseDto>> getApplicationForms(@Valid @ModelAttribute SearchApplicationFormRequestDto searchApplicationFormRequestDto) {
		return ResponseEntity.ok(applicationApplicationFormFacade.getApplicationFormsWithoutItem(searchApplicationFormRequestDto.toCommand()));
	}

}
