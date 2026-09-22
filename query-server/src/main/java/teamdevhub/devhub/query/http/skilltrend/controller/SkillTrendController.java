package teamdevhub.devhub.query.http.skilltrend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import teamdevhub.devhub.query.http.skilltrend.model.response.SkillTrendResponseDto;
import teamdevhub.devhub.web.api.model.response.DataApiResponseDto;
import teamdevhub.devhub.query.http.skilltrend.facade.SkillTrendFacade;
import teamdevhub.devhub.web.enums.SuccessCode;

@Tag(name = "Skill Trend", description = "스킬 트렌드 통계 데이터 API")
@RestController
@RequestMapping("/skill-trends")
@RequiredArgsConstructor
public class SkillTrendController {

    private final SkillTrendFacade skillTrendFacade;

    @Operation(summary = "스킬 트렌드 조회", description = "카드 통계, 수요 스킬, 인기 포지션, 월별 타임라인 등 스킬 트렌드 데이터를 반환합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공")
    @GetMapping
    public ResponseEntity<DataApiResponseDto<SkillTrendResponseDto>> getSkillTrendData() {
        return ResponseEntity.ok(
                DataApiResponseDto.successWithData(
                        SuccessCode.READ_SUCCESS,
                        skillTrendFacade.getSkillTrendData()
                )
        );
    }
}
