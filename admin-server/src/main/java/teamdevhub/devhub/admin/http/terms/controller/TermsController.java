package teamdevhub.devhub.admin.http.terms.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import teamdevhub.devhub.admin.http.terms.model.CreateTermsRequestDto;
import teamdevhub.devhub.web.api.web.model.response.DataApiResponseDto;
import teamdevhub.devhub.admin.http.response.DataListApiResponseDto;
import teamdevhub.devhub.admin.http.terms.port.in.facade.TermsFacade;
import teamdevhub.devhub.admin.http.terms.port.in.facade.model.TermsResponseDto;
import teamdevhub.devhub.web.shared.enums.SuccessCode;

import java.util.List;

@Tag(name = "Terms", description = "약관 조회 및 등록 API")
@RestController
@RequestMapping("/terms")
@RequiredArgsConstructor
public class TermsController {

    private final TermsFacade termsFacade;

    @Operation(summary = "약관 목록 조회", description = "전체 약관 목록을 반환합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공")
    @GetMapping()
    public ResponseEntity<DataListApiResponseDto<TermsResponseDto>> list() {
        List<TermsResponseDto> termsList = termsFacade.listTerms().stream()
                .map(TermsResponseDto::fromDomain)
                .toList();

        return ResponseEntity.ok(
                DataListApiResponseDto.successWithDataList(
                        SuccessCode.READ_SUCCESS,
                        termsList
                )
        );
    }

    @Operation(summary = "약관 등록", description = "새로운 약관을 등록합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "등록 성공"),
            @ApiResponse(responseCode = "400", description = "유효성 검사 실패")
    })
    @PostMapping()
    public ResponseEntity<DataApiResponseDto<Void>> register(@Valid @RequestBody CreateTermsRequestDto createTermsRequestDto) {
        termsFacade.registerTerms(createTermsRequestDto.toCreateTermsCommand());
        return ResponseEntity.ok(
                DataApiResponseDto.successWithoutData(
                        SuccessCode.CREATE_SUCCESS
                )
        );
    }
}

