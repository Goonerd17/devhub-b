package teamdevhub.devhub.web.api.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import teamdevhub.devhub.web.api.board.model.SearchBoardRequestDto;
import teamdevhub.devhub.web.api.user.model.UpdateProfileImageRequestDto;
import teamdevhub.devhub.web.api.user.model.UpdateProfileRequestDto;
import teamdevhub.devhub.web.api.web.model.request.PageRequestDto;
import teamdevhub.devhub.web.api.web.model.response.DataApiResponseDto;
import teamdevhub.devhub.web.api.web.model.response.DataListApiResponseDto;
import teamdevhub.devhub.web.api.web.model.response.PageResponseDto;
import teamdevhub.devhub.web.api.web.resolver.LoginUser;
import teamdevhub.devhub.identity.core.auth.domain.vo.user.AuthenticatedUser;
import teamdevhub.devhub.web.api.board.facade.BoardFacade;
import teamdevhub.devhub.web.core.board.port.in.Facade.model.BoardSummaryResponseDto;
import teamdevhub.devhub.platform.core.common.page.PageCommand;
import teamdevhub.devhub.platform.core.common.page.PageResult;
import teamdevhub.devhub.web.core.project.port.in.facade.ProjectFacade;
import teamdevhub.devhub.web.core.project.port.in.facade.model.UserProjectResponseDto;
import teamdevhub.devhub.web.core.user.port.in.facade.UserProfileFacade;
import teamdevhub.devhub.web.core.user.port.in.facade.UserWithdrawFacade;
import teamdevhub.devhub.web.core.user.port.in.facade.model.UserBasicResponseDto;
import teamdevhub.devhub.web.core.user.port.in.facade.model.UserDetailResponseDto;
import teamdevhub.devhub.web.shared.enums.SuccessCode;

@Tag(name = "User - Profile", description = "사용자 프로필 조회 및 수정 API")
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserProfileController {

    private final UserProfileFacade userProfileFacade;
    private final UserWithdrawFacade userWithdrawFacade;
    private final BoardFacade boardFacade;
    private final ProjectFacade projectFacade;

    @Operation(summary = "내 기본 정보 조회", description = "로그인된 사용자의 기본 정보(이름, GUID 등)를 반환합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공")
    @GetMapping()
    public ResponseEntity<DataApiResponseDto<UserBasicResponseDto>> getUserInfo(@LoginUser AuthenticatedUser authenticatedUser) {
        return ResponseEntity.ok(
                DataApiResponseDto.successWithData(
                        SuccessCode.READ_SUCCESS,
                        userProfileFacade.getUserInfo(authenticatedUser.userGuid())
                )
        );
    }

    @Operation(summary = "내 프로필 상세 조회", description = "로그인된 사용자의 상세 프로필(소개, 포지션, 스킬 등)을 반환합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공")
    @GetMapping("/profile")
    public ResponseEntity<DataApiResponseDto<UserDetailResponseDto>> getProfile(@LoginUser AuthenticatedUser authenticatedUser) {
        return ResponseEntity.ok(
                DataApiResponseDto.successWithData(
                        SuccessCode.READ_SUCCESS,
                        userProfileFacade.getCurrentUserProfile(authenticatedUser.userGuid())
                )
        );
    }

    @Operation(summary = "프로필 이미지 변경", description = "로그인된 사용자의 프로필 이미지 URL을 변경합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "이미지 변경 성공"),
            @ApiResponse(responseCode = "400", description = "유효하지 않은 이미지 URL")
    })
    @PostMapping("/profile/image")
    public ResponseEntity<DataApiResponseDto<Void>> updateProfileImage(@Valid @RequestBody UpdateProfileImageRequestDto updateProfileImageRequestDto, @LoginUser AuthenticatedUser authenticatedUser) {
        userProfileFacade.updateProfileImage(updateProfileImageRequestDto.toUpdateProfileImageCommand(authenticatedUser.userGuid()));
        return ResponseEntity.ok(
                DataApiResponseDto.successWithoutData(
                        SuccessCode.UPDATE_SUCCESS
                )
        );
    }

    @Operation(summary = "프로필 수정", description = "로그인된 사용자의 소개, 포지션, 스킬 등 프로필 정보를 수정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "프로필 수정 성공"),
            @ApiResponse(responseCode = "400", description = "유효성 검사 실패")
    })
    @PutMapping("/profile")
    public ResponseEntity<DataApiResponseDto<Void>> updateProfile(@Valid @RequestBody UpdateProfileRequestDto updateProfileRequestDto, @LoginUser AuthenticatedUser authenticatedUser) {
        userProfileFacade.updateProfile(updateProfileRequestDto.toUpdateProfileCommand(authenticatedUser.userGuid()));
        return ResponseEntity.ok(
                DataApiResponseDto.successWithoutData(
                        SuccessCode.UPDATE_SUCCESS
                )
        );
    }

    @Operation(summary = "회원 탈퇴", description = "로그인된 사용자의 계정을 소프트 삭제(탈퇴 처리)합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "탈퇴 처리 성공"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자")
    })
    @DeleteMapping("/profile")
    public ResponseEntity<DataApiResponseDto<Void>> withdraw(@LoginUser AuthenticatedUser authenticatedUser) {
        userWithdrawFacade.withdraw(authenticatedUser.userGuid());
        return ResponseEntity.ok(
                DataApiResponseDto.successWithoutData(
                        SuccessCode.USER_DELETE_SUCCESS
                )
        );
    }
    
    @Operation(summary = "내 게시글 목록 조회", description = "로그인된 사용자가 작성한 게시글 목록을 페이징으로 반환합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공")
    @GetMapping("/profile/boards")
    public ResponseEntity<DataListApiResponseDto<BoardSummaryResponseDto>> getUserListBoard(@ModelAttribute SearchBoardRequestDto searchBoardRequestDto, PageRequestDto pageRequestDto,
    		@LoginUser AuthenticatedUser authenticatedUser) {
    	return ResponseEntity.ok(boardFacade.listBoard(searchBoardRequestDto.toCommand(authenticatedUser.userGuid()), PageCommand.of((pageRequestDto.getPage()), pageRequestDto.getSize()), authenticatedUser.userGuid()));
    }
    
    @Operation(summary = "내 프로젝트 목록 조회", description = "로그인된 사용자가 참여 중인 프로젝트 목록을 페이징으로 반환합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공")
    @GetMapping("/projects")
    public ResponseEntity<DataListApiResponseDto<UserProjectResponseDto>> getUserProjects(
            @Parameter(description = "페이지 번호 (0부터 시작)", example = "0") @RequestParam("page") int page,
            @Parameter(description = "페이지 크기", example = "10") @RequestParam("size") int size,
    		@LoginUser AuthenticatedUser authenticatedUser) {
        PageResult<UserProjectResponseDto> pagedProjects = projectFacade.getUserProjects(authenticatedUser.userGuid(), PageCommand.of(page, size));
        return ResponseEntity.ok(
        		DataListApiResponseDto.successWithDataList(
                        SuccessCode.READ_SUCCESS,
                        pagedProjects.content(),
                        PageResponseDto.from(pagedProjects)
                )
        );
    }
    
    @Operation(summary = "내 좋아요 프로젝트 목록 조회", description = "로그인된 사용자가 좋아요한 프로젝트 목록을 페이징으로 반환합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공")
    @GetMapping("/projects/likes")
    public ResponseEntity<DataListApiResponseDto<UserProjectResponseDto>> getUserLikeProjects(
            @Parameter(description = "페이지 번호 (0부터 시작)", example = "0") @RequestParam("page") int page,
            @Parameter(description = "페이지 크기", example = "10") @RequestParam("size") int size,
    		@LoginUser AuthenticatedUser authenticatedUser) {
        PageResult<UserProjectResponseDto> pagedProjects = projectFacade.getUserLikeProjects(authenticatedUser.userGuid(), PageCommand.of(page, size));
        return ResponseEntity.ok(
        		DataListApiResponseDto.successWithDataList(
                        SuccessCode.READ_SUCCESS,
                        pagedProjects.content(),
                        PageResponseDto.from(pagedProjects)
                )
        );
    }
    
    @Operation(summary = "내 지원 프로젝트 목록 조회", description = "로그인된 사용자가 지원한 프로젝트 목록을 페이징으로 반환합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공")
    @GetMapping("/projects/applications")
    public ResponseEntity<DataListApiResponseDto<UserProjectResponseDto>> getUserApplyProjects(
            @Parameter(description = "페이지 번호 (0부터 시작)", example = "0") @RequestParam("page") int page,
            @Parameter(description = "페이지 크기", example = "10") @RequestParam("size") int size,
    		@LoginUser AuthenticatedUser authenticatedUser) {
        PageResult<UserProjectResponseDto> pagedProjects = projectFacade.getUserApplyProjects(authenticatedUser.userGuid(), PageCommand.of(page, size));
        return ResponseEntity.ok(
        		DataListApiResponseDto.successWithDataList(
                        SuccessCode.READ_SUCCESS,
                        pagedProjects.content(),
                        PageResponseDto.from(pagedProjects)
                )
        );
    }

    @Operation(summary = "내 참여 프로젝트 목록 조회", description = "로그인된 사용자가 참여한 프로젝트 목록을 페이징으로 반환합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공")
    @GetMapping("/projects/participates")
    public ResponseEntity<DataListApiResponseDto<UserProjectResponseDto>> getUserParticipateProjects(@RequestParam("page") int page, @RequestParam("size") int size,
    		@LoginUser AuthenticatedUser authenticatedUser) {
        PageResult<UserProjectResponseDto> pagedProjects = projectFacade.getUserParticipateProjects(authenticatedUser.userGuid(), PageCommand.of(page, size));
        return ResponseEntity.ok(
        		DataListApiResponseDto.successWithDataList(
                        SuccessCode.READ_SUCCESS,
                        pagedProjects.content(),
                        PageResponseDto.from(pagedProjects)
                )
        );
    }
}
