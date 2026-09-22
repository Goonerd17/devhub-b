package teamdevhub.devhub.member.http.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import teamdevhub.devhub.member.http.user.facade.UserProfileHttpFacade;
import teamdevhub.devhub.member.http.user.model.*;
import teamdevhub.devhub.web.api.web.model.response.DataApiResponseDto;
import teamdevhub.devhub.shared.security.CurrentUserPrincipal;
import teamdevhub.devhub.web.shared.enums.SuccessCode;

@Tag(name = "User - Profile", description = "사용자 프로필 API")
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserProfileController {
    private final UserProfileHttpFacade facade;

    @Operation(summary = "내 기본 정보 조회")
    @GetMapping
    public ResponseEntity<DataApiResponseDto<UserBasicResponseDto>> getUserInfo(@AuthenticationPrincipal CurrentUserPrincipal user) {
        return ResponseEntity.ok(DataApiResponseDto.successWithData(SuccessCode.READ_SUCCESS, facade.getUserInfo(user.userGuid())));
    }

    @Operation(summary = "내 프로필 상세 조회")
    @GetMapping("/profile")
    public ResponseEntity<DataApiResponseDto<UserDetailResponseDto>> getProfile(@AuthenticationPrincipal CurrentUserPrincipal user) {
        return ResponseEntity.ok(DataApiResponseDto.successWithData(SuccessCode.READ_SUCCESS, facade.getProfile(user.userGuid())));
    }

    @Operation(summary = "프로필 이미지 변경")
    @PutMapping("/profile/image")
    public ResponseEntity<DataApiResponseDto<Void>> updateProfileImage(@Valid @RequestBody UpdateProfileImageRequestDto request, @AuthenticationPrincipal CurrentUserPrincipal user) {
        facade.updateProfileImage(request.toUpdateProfileImageCommand(user.userGuid()));
        return ResponseEntity.ok(DataApiResponseDto.successWithoutData(SuccessCode.UPDATE_SUCCESS));
    }

    @Operation(summary = "프로필 수정")
    @PutMapping("/profile")
    public ResponseEntity<DataApiResponseDto<Void>> updateProfile(@Valid @RequestBody UpdateProfileRequestDto request, @AuthenticationPrincipal CurrentUserPrincipal user) {
        facade.updateProfile(request.toUpdateProfileCommand(user.userGuid()));
        return ResponseEntity.ok(DataApiResponseDto.successWithoutData(SuccessCode.UPDATE_SUCCESS));
    }
}
