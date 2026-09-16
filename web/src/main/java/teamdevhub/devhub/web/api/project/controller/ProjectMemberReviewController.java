package teamdevhub.devhub.web.api.project.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import teamdevhub.devhub.web.api.project.model.ReviewUserRequestDto;
import teamdevhub.devhub.web.api.web.model.response.DataApiResponseDto;
import teamdevhub.devhub.web.api.web.resolver.LoginUser;
import teamdevhub.devhub.identity.core.auth.domain.vo.user.AuthenticatedUser;
import teamdevhub.devhub.web.core.user.port.in.facade.UserReviewFacade;
import teamdevhub.devhub.web.shared.enums.SuccessCode;

@Tag(name = "Project - Review", description = "프로젝트 팀원 리뷰 API")
@RestController
@RequestMapping("/projects")
@RequiredArgsConstructor
public class ProjectMemberReviewController {

    private final UserReviewFacade userReviewFacade;

    @Operation(summary = "팀원 리뷰", description = "완료된 프로젝트의 팀원에게 매너 점수 리뷰를 남깁니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "리뷰 성공"),
            @ApiResponse(responseCode = "400", description = "유효성 검사 실패"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자")
    })
    @PostMapping("/{projectGuid}/members/{userGuid}")
    public ResponseEntity<DataApiResponseDto<Void>> reviewMember(
            @Parameter(description = "프로젝트 GUID", required = true) @PathVariable String projectGuid,
            @Parameter(description = "리뷰 대상 사용자 GUID", required = true) @PathVariable String userGuid,
            @Valid @RequestBody ReviewUserRequestDto reviewUserRequestDto,
            @LoginUser AuthenticatedUser authenticatedUser) {

        userReviewFacade.reviewMember(reviewUserRequestDto.toReviewUserCommand(projectGuid, userGuid, reviewUserRequestDto, authenticatedUser));
        return ResponseEntity.ok(DataApiResponseDto.successWithoutData(SuccessCode.REVIEW_SUCCESS));
    }
}
