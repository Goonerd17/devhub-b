package teamdevhub.devhub.project.http.project.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import teamdevhub.devhub.project.http.project.facade.ProjectFacade;
import teamdevhub.devhub.project.http.project.model.UserProjectResponseDto;
import teamdevhub.devhub.project.http.response.DataListApiResponseDto;
import teamdevhub.devhub.project.http.response.PageResponseDto;
import teamdevhub.devhub.shared.core.common.page.PageCommand;
import teamdevhub.devhub.shared.security.CurrentUserPrincipal;
import teamdevhub.devhub.web.enums.SuccessCode;

@Tag(name = "User - Projects", description = "사용자 프로젝트 목록 API")
@RestController
@RequestMapping("/user/projects")
@RequiredArgsConstructor
public class UserProjectController {
    private final ProjectFacade projectFacade;

    @Operation(summary = "내 등록 프로젝트 목록 조회")
    @GetMapping
    public ResponseEntity<DataListApiResponseDto<UserProjectResponseDto>> registered(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal CurrentUserPrincipal user) {
        return list(projectFacade.getUserProjects(user.userGuid(), PageCommand.of(page, size)));
    }

    @Operation(summary = "내 좋아요 프로젝트 목록 조회")
    @GetMapping("/likes")
    public ResponseEntity<DataListApiResponseDto<UserProjectResponseDto>> liked(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal CurrentUserPrincipal user) {
        return list(projectFacade.getUserLikeProjects(user.userGuid(), PageCommand.of(page, size)));
    }

    @Operation(summary = "내 지원 프로젝트 목록 조회")
    @GetMapping("/applications")
    public ResponseEntity<DataListApiResponseDto<UserProjectResponseDto>> applied(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal CurrentUserPrincipal user) {
        return list(projectFacade.getUserApplyProjects(user.userGuid(), PageCommand.of(page, size)));
    }

    @Operation(summary = "내 참여 프로젝트 목록 조회")
    @GetMapping("/participates")
    public ResponseEntity<DataListApiResponseDto<UserProjectResponseDto>> participated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal CurrentUserPrincipal user) {
        return list(projectFacade.getUserParticipateProjects(user.userGuid(), PageCommand.of(page, size)));
    }

    private ResponseEntity<DataListApiResponseDto<UserProjectResponseDto>> list(teamdevhub.devhub.shared.core.common.page.PageResult<UserProjectResponseDto> result) {
        return ResponseEntity.ok(DataListApiResponseDto.successWithDataList(SuccessCode.READ_SUCCESS, result.content(), PageResponseDto.from(result)));
    }
}
