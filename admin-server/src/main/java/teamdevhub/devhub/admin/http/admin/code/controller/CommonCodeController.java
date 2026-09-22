package teamdevhub.devhub.admin.http.admin.code.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import teamdevhub.devhub.admin.http.admin.code.model.request.CommonCodeRequestDto;
import teamdevhub.devhub.admin.http.admin.code.model.response.CommonCodeResponseDto;
import teamdevhub.devhub.web.api.model.response.DataApiResponseDto;
import teamdevhub.devhub.admin.http.response.DataListApiResponseDto;
import teamdevhub.devhub.admin.http.admin.code.facade.CommonCodeFacade;

@Tag(name = "Admin - CommonCode", description = "관리자 공통 코드 관리 API")
@RestController
@RequestMapping("/admin/code")
@RequiredArgsConstructor
public class CommonCodeController {

    private final CommonCodeFacade commonCodeFacade;

    @Operation(summary = "공통 코드 목록 조회", description = "전체 공통 코드 목록을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공")
    @GetMapping("/list")
    public ResponseEntity<DataListApiResponseDto<CommonCodeResponseDto>> getCommonCodeList() {
        return ResponseEntity.ok(commonCodeFacade.getCommonCodeList());
    }

    @Operation(summary = "공통 코드 저장 (등록/수정)", description = "공통 코드를 등록하거나 수정합니다.")
    @ApiResponse(responseCode = "200", description = "저장 성공")
    @PutMapping
    public ResponseEntity<DataApiResponseDto<?>> saveCommonCode(@RequestBody CommonCodeRequestDto code) {
        return ResponseEntity.ok(commonCodeFacade.saveCode(code.toCommonCodeCommand(), code.isInsert()));
    }
}
