package teamdevhub.devhub.query.http.home.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import teamdevhub.devhub.query.http.home.model.response.HomeResponseDto;
import teamdevhub.devhub.web.api.web.model.response.DataApiResponseDto;
import teamdevhub.devhub.query.http.home.facade.HomeFacade;
import teamdevhub.devhub.web.shared.enums.SuccessCode;

@Tag(name = "Home", description = "홈 페이지 통합 데이터 API")
@RestController
@RequestMapping("/home")
@RequiredArgsConstructor
public class HomeController {

    private final HomeFacade homeFacade;

    @Operation(summary = "홈 데이터 조회", description = "홈 페이지에 필요한 배너, 프로젝트, 게시글 데이터를 반환합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공")
    @GetMapping
    public ResponseEntity<DataApiResponseDto<HomeResponseDto>> getHomeData() {
        return ResponseEntity.ok(
                DataApiResponseDto.successWithData(
                        SuccessCode.READ_SUCCESS,
                        homeFacade.getHomeData()
                )
        );
    }
}
