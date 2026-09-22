package teamdevhub.devhub.admin.http.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import teamdevhub.devhub.admin.http.response.DataListApiResponseDto;
import teamdevhub.devhub.admin.http.response.PageResponseDto;
import teamdevhub.devhub.admin.http.user.facade.AdminUserHttpFacade;
import teamdevhub.devhub.admin.http.user.model.*;
import teamdevhub.devhub.shared.core.common.page.PageCommand;
import teamdevhub.devhub.web.api.web.model.response.DataApiResponseDto;
import teamdevhub.devhub.web.shared.enums.SuccessCode;

@Tag(name = "Admin - User", description = "관리자 사용자 관리 API")
@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
public class AdminUserController {
    private final AdminUserHttpFacade facade;

    @Operation(summary = "사용자 목록 조회")
    @GetMapping
    public ResponseEntity<DataListApiResponseDto<UserBasicResponseDto>> list(@ModelAttribute SearchUserRequestDto request, @RequestParam(defaultValue="0") int page, @RequestParam(defaultValue="10") int size) {
        var result = facade.listUsers(request.toSearchUserCommand(), PageCommand.of(page, size));
        return ResponseEntity.ok(DataListApiResponseDto.successWithDataList(SuccessCode.READ_SUCCESS, result.content(), PageResponseDto.from(result)));
    }

    @Operation(summary = "사용자 상세 조회")
    @GetMapping("/{userGuid}")
    public ResponseEntity<DataApiResponseDto<AdminUserDetailResponseDto>> detail(@PathVariable String userGuid) {
        return ResponseEntity.ok(DataApiResponseDto.successWithData(SuccessCode.READ_SUCCESS, facade.getUserDetail(userGuid)));
    }

    @PutMapping("/{userGuid}")
    public ResponseEntity<DataApiResponseDto<Void>> update(@PathVariable String userGuid, @Valid @RequestBody AdminUpdateUserRequestDto request) {
        facade.updateUser(request.toAdminUpdateUserCommand(userGuid));
        return ResponseEntity.ok(DataApiResponseDto.successWithoutData(SuccessCode.UPDATE_SUCCESS));
    }

    @PostMapping("/{userGuid}/ban")
    public ResponseEntity<DataApiResponseDto<Void>> ban(@PathVariable String userGuid, @Valid @RequestBody AdminBanUserRequestDto request) {
        facade.banUser(request.toBanUserCommand(userGuid));
        return ResponseEntity.ok(DataApiResponseDto.successWithoutData(SuccessCode.BAN_SUCCESS));
    }

    @PostMapping("/{userGuid}/unban")
    public ResponseEntity<DataApiResponseDto<Void>> unban(@PathVariable String userGuid) {
        facade.unbanUser(userGuid);
        return ResponseEntity.ok(DataApiResponseDto.successWithoutData(SuccessCode.UNBAN_SUCCESS));
    }

    @PostMapping("/{userGuid}/password")
    public ResponseEntity<DataApiResponseDto<Void>> resetPassword(@PathVariable String userGuid, @Valid @RequestBody AdminResetPasswordRequestDto request) {
        facade.resetPassword(userGuid, request.getNewPassword());
        return ResponseEntity.ok(DataApiResponseDto.successWithoutData(SuccessCode.PASSWORD_RESET_SUCCESS));
    }
}
