package teamdevhub.devhub.admin.http.admin.banner.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import teamdevhub.devhub.admin.http.admin.banner.model.request.BannerRequsetDto;
import teamdevhub.devhub.admin.http.admin.banner.model.request.SearchBannerRequestDto;
import teamdevhub.devhub.admin.http.admin.banner.model.response.BannerResponseDto;
import teamdevhub.devhub.web.api.web.model.response.DataApiResponseDto;
import teamdevhub.devhub.admin.http.response.DataListApiResponseDto;
import teamdevhub.devhub.admin.http.admin.banner.facade.BannerFacade;
import teamdevhub.devhub.shared.core.common.page.PageCommand;

@Tag(name = "Admin - Banner", description = "관리자 배너 관리 API")
@RestController
@RequestMapping("/admin/banner")
@RequiredArgsConstructor
public class BannerController {

    private final BannerFacade bannerFacade;

    @Operation(summary = "배너 목록 조회", description = "검색 조건으로 배너 목록을 페이징 조회합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공")
    @GetMapping("/list")
    public ResponseEntity<DataListApiResponseDto<BannerResponseDto>> getBannerList(
            @Valid @ModelAttribute SearchBannerRequestDto searchBannerRequestDto,
            @Parameter(description = "페이지 번호", example = "0") @RequestParam("page") int page,
            @Parameter(description = "페이지 크기", example = "10") @RequestParam("size") int size) {
        return ResponseEntity.ok(bannerFacade.getBannerList(searchBannerRequestDto.toCommand(), PageCommand.of(page, size)));
    }

    @Operation(summary = "배너 등록", description = "새 배너를 등록합니다.")
    @ApiResponse(responseCode = "200", description = "등록 성공")
    @PutMapping
    public ResponseEntity<DataApiResponseDto<?>> insertBanner(@Valid @RequestBody BannerRequsetDto bannerRequsetDto) {
        return ResponseEntity.ok(bannerFacade.saveBanner(bannerRequsetDto.toCommand()));
    }

    @Operation(summary = "배너 수정", description = "배너 GUID로 배너 정보를 수정합니다.")
    @ApiResponse(responseCode = "200", description = "수정 성공")
    @PutMapping("/{bannerGuid}")
    public ResponseEntity<DataApiResponseDto<?>> updateBanner(
            @Parameter(description = "배너 GUID", required = true) @PathVariable String bannerGuid,
            @Valid @RequestBody BannerRequsetDto bannerRequsetDto) {
        return ResponseEntity.ok(bannerFacade.saveBanner(bannerRequsetDto.toCommand(bannerGuid)));
    }

    @Operation(summary = "배너 삭제", description = "배너 GUID로 배너를 삭제합니다.")
    @ApiResponse(responseCode = "200", description = "삭제 성공")
    @DeleteMapping("/{bannerGuid}")
    public ResponseEntity<DataApiResponseDto<?>> deleteBanner(
            @Parameter(description = "배너 GUID", required = true) @PathVariable String bannerGuid) {
        return ResponseEntity.ok(bannerFacade.deleteBanner(bannerGuid));
    }
}

