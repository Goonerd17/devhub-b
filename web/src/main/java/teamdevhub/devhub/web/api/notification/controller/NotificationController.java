package teamdevhub.devhub.web.api.notification.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import teamdevhub.devhub.web.api.web.model.response.DataApiResponseDto;
import teamdevhub.devhub.web.api.web.model.response.DataListApiResponseDto;
import teamdevhub.devhub.web.api.web.resolver.LoginUser;
import teamdevhub.devhub.web.api.notification.facade.NotificationFacade;
import teamdevhub.devhub.web.core.notification.port.in.facade.model.NotificationResponseDto;
import teamdevhub.devhub.identity.core.auth.domain.vo.user.AuthenticatedUser;

@Tag(name = "Notification", description = "알림 조회/확인 API")
@RestController
@RequestMapping("/notification")
@RequiredArgsConstructor
public class NotificationController {

	private final NotificationFacade notificationFacade;

	@Operation(summary = "알림 목록 조회", description = "로그인된 사용자의 알림 목록을 반환합니다.")
	@ApiResponse(responseCode = "200", description = "조회 성공")
	@GetMapping("/list")
    public ResponseEntity<DataListApiResponseDto<NotificationResponseDto>> getNotificationList(@LoginUser AuthenticatedUser authenticatedUser) {
        return ResponseEntity.ok(notificationFacade.getNotificationList(authenticatedUser.userGuid()));
    }

    @Operation(summary = "알림 읽음 처리", description = "알림 GUID로 알림을 읽음 처리합니다.")
    @ApiResponse(responseCode = "200", description = "읽음 처리 성공")
    @PutMapping("/checked/{notificationGuid}")
    public ResponseEntity<DataApiResponseDto<Void>> checkedNotification(
            @LoginUser AuthenticatedUser authenticatedUser,
            @Parameter(description = "알림 GUID", required = true) @PathVariable String notificationGuid) {
        return ResponseEntity.ok(notificationFacade.checkedNotification(notificationGuid));
    }
}
