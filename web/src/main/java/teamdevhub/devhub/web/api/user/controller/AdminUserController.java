package teamdevhub.devhub.web.api.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import teamdevhub.devhub.web.api.user.model.*;
import teamdevhub.devhub.web.api.web.model.response.DataApiResponseDto;
import teamdevhub.devhub.web.api.web.model.response.DataListApiResponseDto;
import teamdevhub.devhub.web.api.web.model.response.PageResponseDto;
import teamdevhub.devhub.platform.core.common.page.PageCommand;
import teamdevhub.devhub.platform.core.common.page.PageResult;
import teamdevhub.devhub.web.core.project.port.in.facade.model.UserProjectResponseDto;
import teamdevhub.devhub.community.api.ReportPage;
import teamdevhub.devhub.member.core.user.domain.User;
import teamdevhub.devhub.web.core.user.port.in.facade.AdminUserFacade;
import teamdevhub.devhub.web.core.user.port.in.facade.model.UserBasicResponseDto;
import teamdevhub.devhub.web.shared.enums.SuccessCode;

import java.util.List;

@Tag(name = "Admin - User", description = "관리자 사용자 관리 API")
@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
public class AdminUserController {

    private final AdminUserFacade adminUserFacade;

    @Operation(summary = "사용자 목록 조회", description = "검색 조건으로 전체 사용자 목록을 페이징 조회합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공")
    @GetMapping
    public ResponseEntity<DataListApiResponseDto<UserBasicResponseDto>> listUsers(
            @ModelAttribute SearchUserRequestDto searchUserRequestDto,
            @Parameter(description = "페이지 번호 (0부터 시작)", example = "0") @RequestParam int page,
            @Parameter(description = "페이지 크기", example = "10") @RequestParam int size) {

        PageResult<UserBasicResponseDto> result = adminUserFacade.listUsers(
                searchUserRequestDto.toSearchUserCommand(), PageCommand.of(page, size));

        return ResponseEntity.ok(
                DataListApiResponseDto.successWithDataList(
                        SuccessCode.READ_SUCCESS,
                        result.content(),
                        PageResponseDto.from(result)));
    }

    @Operation(summary = "사용자 상세 조회", description = "특정 사용자의 상세 정보를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공")
    @GetMapping("/{userGuid}")
    public ResponseEntity<DataApiResponseDto<AdminUserDetailResponseDto>> getUserDetail(
            @Parameter(description = "사용자 GUID") @PathVariable("userGuid") String userGuid) {
        User user = adminUserFacade.getUserDetail(userGuid);
        return ResponseEntity.ok(DataApiResponseDto.successWithData(SuccessCode.READ_SUCCESS, AdminUserDetailResponseDto.fromDomain(user)));
    }

    @Operation(summary = "사용자 정보 수정", description = "특정 사용자의 닉네임과 자기소개를 수정합니다.")
    @ApiResponse(responseCode = "200", description = "수정 성공")
    @PutMapping("/{userGuid}")
    public ResponseEntity<DataApiResponseDto<Void>> updateUser(
            @Parameter(description = "사용자 GUID") @PathVariable String userGuid,
            @Valid @RequestBody AdminUpdateUserRequestDto adminUpdateUserRequestDto) {
        adminUserFacade.updateUser(adminUpdateUserRequestDto.toAdminUpdateUserCommand(userGuid));
        return ResponseEntity.ok(DataApiResponseDto.successWithoutData(SuccessCode.UPDATE_SUCCESS));
    }

    @Operation(summary = "비밀번호 초기화", description = "특정 사용자의 비밀번호를 관리자가 초기화합니다.")
    @ApiResponse(responseCode = "200", description = "초기화 성공")
    @PostMapping("/{userGuid}/password")
    public ResponseEntity<DataApiResponseDto<Void>> resetPassword(
            @Parameter(description = "사용자 GUID") @PathVariable String userGuid,
            @Valid @RequestBody AdminResetPasswordRequestDto adminResetPasswordRequestDto) {
        adminUserFacade.resetUserPassword(userGuid, adminResetPasswordRequestDto.getNewPassword());
        return ResponseEntity.ok(DataApiResponseDto.successWithoutData(SuccessCode.PASSWORD_RESET_SUCCESS));
    }

    @Operation(summary = "사용자 정지", description = "특정 사용자를 정지 처리합니다.")
    @ApiResponse(responseCode = "200", description = "정지 성공")
    @PostMapping("/{userGuid}/ban")
    public ResponseEntity<DataApiResponseDto<Void>> banUser(
            @Parameter(description = "사용자 GUID") @PathVariable String userGuid,
            @Valid @RequestBody AdminBanUserRequestDto adminBanUserRequestDto) {
        adminUserFacade.banUser(adminBanUserRequestDto.toBanUserCommand(userGuid));
        return ResponseEntity.ok(DataApiResponseDto.successWithoutData(SuccessCode.BAN_SUCCESS));
    }

    @Operation(summary = "사용자 정지 해제", description = "정지된 사용자의 정지를 해제합니다.")
    @ApiResponse(responseCode = "200", description = "정지 해제 성공")
    @PostMapping("/{userGuid}/unban")
    public ResponseEntity<DataApiResponseDto<Void>> unbanUser(
            @Parameter(description = "사용자 GUID") @PathVariable String userGuid) {
        adminUserFacade.unbanUser(userGuid);
        return ResponseEntity.ok(DataApiResponseDto.successWithoutData(SuccessCode.UNBAN_SUCCESS));
    }

    @Operation(summary = "사용자 등록 프로젝트 조회", description = "특정 사용자가 등록한 프로젝트 목록을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공")
    @GetMapping("/{userGuid}/projects")
    public ResponseEntity<DataListApiResponseDto<UserProjectResponseDto>> getUserProjects(
            @Parameter(description = "사용자 GUID") @PathVariable("userGuid") String userGuid,
            @Parameter(description = "페이지 번호 (0부터 시작)", example = "0") @RequestParam("page") int page,
            @Parameter(description = "페이지 크기", example = "10") @RequestParam("size") int size) {
    	PageResult<UserProjectResponseDto> projects = adminUserFacade.getUserProjects(
                userGuid, PageCommand.of(page, size));
        return ResponseEntity.ok(
                DataListApiResponseDto.successWithDataList(SuccessCode.READ_SUCCESS, projects.content(), PageResponseDto.from(projects)));
    }

    @Operation(summary = "사용자 지원 프로젝트 조회", description = "특정 사용자가 지원한 프로젝트 목록을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공")
    @GetMapping("/{userGuid}/projects/applicant")
    public ResponseEntity<DataListApiResponseDto<UserProjectResponseDto>> getUserApplyProjects(
            @Parameter(description = "사용자 GUID") @PathVariable("userGuid") String userGuid,
            @Parameter(description = "페이지 번호 (0부터 시작)", example = "0") @RequestParam("page") int page,
            @Parameter(description = "페이지 크기", example = "10") @RequestParam("size") int size) {
    	PageResult<UserProjectResponseDto> projects = adminUserFacade.getUserApplyProjects(
                userGuid, PageCommand.of(page, size));
        return ResponseEntity.ok(
                DataListApiResponseDto.successWithDataList(SuccessCode.READ_SUCCESS, projects.content(), PageResponseDto.from(projects)));
    }

    @Operation(summary = "사용자가 받은 신고 조회", description = "특정 사용자가 신고를 받은 내역을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공")
    @GetMapping("/{userGuid}/reports")
    public ResponseEntity<DataListApiResponseDto<AdminReportResponseDto>> getUserReceivedReports(
            @Parameter(description = "사용자 GUID") @PathVariable String userGuid,
            @Parameter(description = "페이지 번호 (0부터 시작)", example = "0") @RequestParam int page,
            @Parameter(description = "페이지 크기", example = "10") @RequestParam int size) {
        ReportPage result = adminUserFacade.getUserReceivedReports(userGuid, PageCommand.of(page, size));
        List<AdminReportResponseDto> dtos = result.content().stream().map(AdminReportResponseDto::fromView).toList();
        return ResponseEntity.ok(
                DataListApiResponseDto.successWithDataList(SuccessCode.READ_SUCCESS, dtos, PageResponseDto.from(result)));
    }

    @Operation(summary = "사용자가 제출한 신고 조회", description = "특정 사용자가 신고한 내역을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공")
    @GetMapping("/{userGuid}/reports/reported")
    public ResponseEntity<DataListApiResponseDto<AdminReportResponseDto>> getUserSubmittedReports(
            @Parameter(description = "사용자 GUID") @PathVariable String userGuid,
            @Parameter(description = "페이지 번호 (0부터 시작)", example = "0") @RequestParam int page,
            @Parameter(description = "페이지 크기", example = "10") @RequestParam int size) {
        ReportPage result = adminUserFacade.getUserSubmittedReports(userGuid, PageCommand.of(page, size));
        List<AdminReportResponseDto> dtos = result.content().stream().map(AdminReportResponseDto::fromView).toList();
        return ResponseEntity.ok(
                DataListApiResponseDto.successWithDataList(SuccessCode.READ_SUCCESS, dtos, PageResponseDto.from(result)));
    }

    @Operation(summary = "전체 신고 목록 조회", description = "모든 신고 내역을 페이징 조회합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공")
    @GetMapping("/reports")
    public ResponseEntity<DataListApiResponseDto<AdminReportResponseDto>> getAllReports(
            @Parameter(description = "페이지 번호 (0부터 시작)", example = "0") @RequestParam int page,
            @Parameter(description = "페이지 크기", example = "10") @RequestParam int size) {
        ReportPage result = adminUserFacade.getAllReports(PageCommand.of(page, size));
        List<AdminReportResponseDto> dtos = result.content().stream().map(AdminReportResponseDto::fromView).toList();
        return ResponseEntity.ok(
                DataListApiResponseDto.successWithDataList(SuccessCode.READ_SUCCESS, dtos, PageResponseDto.from(result)));
    }

    @Operation(summary = "신고 처리 완료 처리", description = "특정 신고를 처리 완료 상태로 변경합니다.")
    @ApiResponse(responseCode = "200", description = "처리 성공")
    @PutMapping("/reports/{reportGuid}/process")
    public ResponseEntity<DataApiResponseDto<Void>> processReport(
            @Parameter(description = "신고 GUID") @PathVariable String reportGuid) {
        adminUserFacade.processReport(reportGuid);
        return ResponseEntity.ok(DataApiResponseDto.successWithoutData(SuccessCode.UPDATE_SUCCESS));
    }
}
