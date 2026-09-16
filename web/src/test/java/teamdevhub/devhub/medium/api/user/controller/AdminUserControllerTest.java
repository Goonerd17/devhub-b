package teamdevhub.devhub.web.api.user.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import teamdevhub.devhub.web.api.user.controller.AdminUserController;
import teamdevhub.devhub.web.api.user.model.*;
import teamdevhub.devhub.web.api.web.model.response.DataApiResponseDto;
import teamdevhub.devhub.web.api.web.model.response.DataListApiResponseDto;
import teamdevhub.devhub.platform.core.common.audit.AuditInfo;
import teamdevhub.devhub.platform.core.common.page.PageCommand;
import teamdevhub.devhub.platform.core.common.page.PageResult;
import teamdevhub.devhub.community.api.ReportPage;
import teamdevhub.devhub.member.core.user.domain.User;
import teamdevhub.devhub.member.core.user.domain.vo.UserRole;
import teamdevhub.devhub.member.core.user.port.in.command.SearchUserCommand;
import teamdevhub.devhub.web.core.user.port.in.facade.AdminUserFacade;
import teamdevhub.devhub.web.core.user.port.in.facade.model.UserBasicResponseDto;
import teamdevhub.devhub.web.shared.enums.SuccessCode;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static teamdevhub.devhub.constant.UserTestConstant.*;

class AdminUserControllerTest {

    private AdminUserController adminUserController;
    private AdminUserFacade adminUserFacade;

    @BeforeEach
    void init() {
        adminUserFacade = Mockito.mock(AdminUserFacade.class);
        adminUserController = new AdminUserController(adminUserFacade);
    }

    @Test
    @DisplayName("사용자_목록_조회_시_READ_SUCCESS_코드와_페이지_정보를_반환한다")
    void listUsers_returnsReadSuccessWithPageInfo() {
        // given
        PageResult<UserBasicResponseDto> pageResult = PageResult.of(
                List.of(UserBasicResponseDto.builder().userGuid(TEST_USER_GUID_1).username(TEST_USERNAME_1).build(),
                        UserBasicResponseDto.builder().userGuid(TEST_USER_GUID_2).username(TEST_USERNAME_2).build()),
                0, 10, 2);
        when(adminUserFacade.listUsers(any(SearchUserCommand.class), any(PageCommand.class)))
                .thenReturn(pageResult);

        SearchUserRequestDto requestDto = SearchUserRequestDto.builder().build();

        // when
        ResponseEntity<DataListApiResponseDto<UserBasicResponseDto>> response =
                adminUserController.listUsers(requestDto, 0, 10);

        // then
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().isSuccess()).isTrue();
        assertThat(response.getBody().getCode()).isEqualTo(SuccessCode.READ_SUCCESS.getCode());
        assertThat(response.getBody().getDataList()).hasSize(2);
        assertThat(response.getBody().getPagination().getTotalElements()).isEqualTo(2);
    }

    @Test
    @DisplayName("사용자_상세_조회_시_READ_SUCCESS_코드와_상세_정보를_반환한다")
    void getUserDetail_returnsReadSuccessWithDetail() {
        // given
        User user = Mockito.mock(User.class);
        when(user.getUserGuid()).thenReturn(TEST_USER_GUID_1);
        when(user.getUsername()).thenReturn(TEST_USERNAME_1);
        when(user.getUserRole()).thenReturn(UserRole.USER);
        when(user.getPositions()).thenReturn(Set.of());
        when(user.getSkills()).thenReturn(Set.of());
        when(user.getAuditInfo()).thenReturn(AuditInfo.empty());
        when(adminUserFacade.getUserDetail(TEST_USER_GUID_1)).thenReturn(user);

        // when
        ResponseEntity<DataApiResponseDto<AdminUserDetailResponseDto>> response =
                adminUserController.getUserDetail(TEST_USER_GUID_1);

        // then
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().isSuccess()).isTrue();
        assertThat(response.getBody().getCode()).isEqualTo(SuccessCode.READ_SUCCESS.getCode());
        assertThat(response.getBody().getData().getUserGuid()).isEqualTo(TEST_USER_GUID_1);
    }

    @Test
    @DisplayName("사용자_정보_수정_시_UPDATE_SUCCESS_코드를_반환한다")
    void updateUser_returnsUpdateSuccess() {
        // given
        AdminUpdateUserRequestDto requestDto = AdminUpdateUserRequestDto.builder()
                .username(NEW_USERNAME).introduction(NEW_INTRO).build();

        // when
        ResponseEntity<DataApiResponseDto<Void>> response =
                adminUserController.updateUser(TEST_USER_GUID_1, requestDto);

        // then
        assertThat(response.getBody().isSuccess()).isTrue();
        assertThat(response.getBody().getCode()).isEqualTo(SuccessCode.UPDATE_SUCCESS.getCode());
        verify(adminUserFacade).updateUser(any());
    }

    @Test
    @DisplayName("비밀번호_초기화_시_PASSWORD_RESET_SUCCESS_코드를_반환한다")
    void resetPassword_returnsPasswordResetSuccess() {
        // given
        AdminResetPasswordRequestDto requestDto = AdminResetPasswordRequestDto.builder()
                .newPassword(TEST_NEW_PASSWORD).build();

        // when
        ResponseEntity<DataApiResponseDto<Void>> response =
                adminUserController.resetPassword(TEST_USER_GUID_1, requestDto);

        // then
        assertThat(response.getBody().isSuccess()).isTrue();
        assertThat(response.getBody().getCode()).isEqualTo(SuccessCode.PASSWORD_RESET_SUCCESS.getCode());
        verify(adminUserFacade).resetUserPassword(TEST_USER_GUID_1, TEST_NEW_PASSWORD);
    }

    @Test
    @DisplayName("사용자_정지_시_BAN_SUCCESS_코드를_반환한다")
    void banUser_returnsBanSuccess() {
        // given
        AdminBanUserRequestDto requestDto = AdminBanUserRequestDto.builder()
                .blockEndDate(TEST_BLOCK_END_DATE).build();

        // when
        ResponseEntity<DataApiResponseDto<Void>> response =
                adminUserController.banUser(TEST_USER_GUID_1, requestDto);

        // then
        assertThat(response.getBody().isSuccess()).isTrue();
        assertThat(response.getBody().getCode()).isEqualTo(SuccessCode.BAN_SUCCESS.getCode());
        verify(adminUserFacade).banUser(any());
    }

    @Test
    @DisplayName("사용자_정지_해제_시_UNBAN_SUCCESS_코드를_반환한다")
    void unbanUser_returnsUnbanSuccess() {
        // when
        ResponseEntity<DataApiResponseDto<Void>> response =
                adminUserController.unbanUser(TEST_USER_GUID_1);

        // then
        assertThat(response.getBody().isSuccess()).isTrue();
        assertThat(response.getBody().getCode()).isEqualTo(SuccessCode.UNBAN_SUCCESS.getCode());
        verify(adminUserFacade).unbanUser(TEST_USER_GUID_1);
    }

    @Test
    @DisplayName("사용자_등록_프로젝트_조회_시_READ_SUCCESS_코드를_반환한다")
    void getUserProjects_returnsReadSuccess() {
        // given
        when(adminUserFacade.getUserProjects(anyString(), any(PageCommand.class)))
                .thenReturn(PageResult.of(List.of(), 0, 10, 0));

        // when
        var response = adminUserController.getUserProjects(TEST_USER_GUID_1, 0, 10);

        // then
        assertThat(response.getBody().isSuccess()).isTrue();
        assertThat(response.getBody().getCode()).isEqualTo(SuccessCode.READ_SUCCESS.getCode());
    }

    @Test
    @DisplayName("사용자_지원_프로젝트_조회_시_READ_SUCCESS_코드를_반환한다")
    void getUserApplyProjects_returnsReadSuccess() {
        // given
        when(adminUserFacade.getUserApplyProjects(anyString(), any(PageCommand.class)))
                .thenReturn(PageResult.of(List.of(), 0, 10, 0));

        // when
        var response = adminUserController.getUserApplyProjects(TEST_USER_GUID_1, 0, 10);

        // then
        assertThat(response.getBody().isSuccess()).isTrue();
        assertThat(response.getBody().getCode()).isEqualTo(SuccessCode.READ_SUCCESS.getCode());
    }

    @Test
    @DisplayName("사용자_받은_신고_조회_시_READ_SUCCESS_코드와_페이지_정보를_반환한다")
    void getUserReceivedReports_returnsReadSuccessWithPageInfo() {
        // given
        ReportPage pageResult = new ReportPage(List.of(), 0, 10, 0, 0, true, true);
        when(adminUserFacade.getUserReceivedReports(anyString(), any(PageCommand.class)))
                .thenReturn(pageResult);

        // when
        ResponseEntity<DataListApiResponseDto<AdminReportResponseDto>> response =
                adminUserController.getUserReceivedReports(TEST_USER_GUID_1, 0, 10);

        // then
        assertThat(response.getBody().isSuccess()).isTrue();
        assertThat(response.getBody().getCode()).isEqualTo(SuccessCode.READ_SUCCESS.getCode());
    }

    @Test
    @DisplayName("사용자_제출한_신고_조회_시_READ_SUCCESS_코드와_페이지_정보를_반환한다")
    void getUserSubmittedReports_returnsReadSuccessWithPageInfo() {
        // given
        ReportPage pageResult = new ReportPage(List.of(), 0, 10, 0, 0, true, true);
        when(adminUserFacade.getUserSubmittedReports(anyString(), any(PageCommand.class)))
                .thenReturn(pageResult);

        // when
        ResponseEntity<DataListApiResponseDto<AdminReportResponseDto>> response =
                adminUserController.getUserSubmittedReports(TEST_USER_GUID_1, 0, 10);

        // then
        assertThat(response.getBody().isSuccess()).isTrue();
        assertThat(response.getBody().getCode()).isEqualTo(SuccessCode.READ_SUCCESS.getCode());
    }

    @Test
    @DisplayName("전체_신고_목록_조회_시_READ_SUCCESS_코드와_페이지_정보를_반환한다")
    void getAllReports_returnsReadSuccessWithPageInfo() {
        // given
        ReportPage pageResult = new ReportPage(List.of(), 0, 10, 0, 0, true, true);
        when(adminUserFacade.getAllReports(any(PageCommand.class))).thenReturn(pageResult);

        // when
        ResponseEntity<DataListApiResponseDto<AdminReportResponseDto>> response =
                adminUserController.getAllReports(0, 10);

        // then
        assertThat(response.getBody().isSuccess()).isTrue();
        assertThat(response.getBody().getCode()).isEqualTo(SuccessCode.READ_SUCCESS.getCode());
    }
}
