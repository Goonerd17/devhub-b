package teamdevhub.devhub.web.api.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import teamdevhub.devhub.web.api.admin.code.model.request.CommonCodeRequestDto;
import teamdevhub.devhub.web.api.web.model.response.CommonCodeResponseDto;
import teamdevhub.devhub.web.api.web.model.response.DataApiResponseDto;
import teamdevhub.devhub.web.api.web.facade.CommonFacade;

import java.util.List;
import java.util.Map;

@Tag(name = "Common", description = "공통 코드 조회/저장 API")
@RestController
@RequestMapping("/common")
@RequiredArgsConstructor
public class CommonController {

	private final CommonFacade commonFacade;

	@Operation(summary = "공통 코드 전체 조회", description = "전체 공통 코드를 그룹별 Map 구조로 반환합니다.")
	@ApiResponse(responseCode = "200", description = "조회 성공")
	@GetMapping("/code")
    public ResponseEntity<DataApiResponseDto<Map<String, CommonCodeResponseDto>>> getCommonCodeList() {
        return ResponseEntity.ok(commonFacade.getCommonCodeList());
	}

	@Operation(summary = "공통 코드 단건 저장", description = "공통 코드 한 건을 저장합니다.")
	@ApiResponse(responseCode = "200", description = "저장 성공")
	@PutMapping("/save")
	public ResponseEntity<DataApiResponseDto<Void>> saveCommonCode(@Valid @RequestBody CommonCodeRequestDto commonCodeRequestDto) {
		commonFacade.saveCommonCode(commonCodeRequestDto.toCommonCodeCommand());
		return ResponseEntity.ok().build();
	}

	@Operation(summary = "공통 코드 일괄 저장", description = "공통 코드 목록을 일괄 저장합니다.")
	@ApiResponse(responseCode = "200", description = "저장 성공")
	@PutMapping("/saveAll")
	public ResponseEntity<DataApiResponseDto<Void>> saveAllCommonCode(@Valid @RequestBody List<CommonCodeRequestDto> commonCodeRequestDtoList) {
		commonFacade.saveCommonCodeList(commonCodeRequestDtoList.stream().map(CommonCodeRequestDto::toCommonCodeCommand).toList());
		return ResponseEntity.ok().build();
	}

}
