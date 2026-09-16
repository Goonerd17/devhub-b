package teamdevhub.devhub.web.api.user.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import teamdevhub.devhub.web.api.user.controller.UserProfileController;
import teamdevhub.devhub.web.api.user.model.UpdateProfileRequestDto;
import teamdevhub.devhub.web.api.board.facade.BoardFacade;
import teamdevhub.devhub.web.core.project.port.in.facade.ProjectFacade;
import teamdevhub.devhub.web.core.user.port.in.facade.model.UserDetailResponseDto;
import teamdevhub.devhub.web.api.web.model.response.DataApiResponseDto;
import teamdevhub.devhub.member.core.user.domain.User;
import teamdevhub.devhub.member.core.user.domain.vo.UserRole;
import teamdevhub.devhub.member.api.MemberRole;
import teamdevhub.devhub.web.core.user.port.in.facade.UserProfileFacade;
import teamdevhub.devhub.web.core.user.port.in.facade.UserWithdrawFacade;
import teamdevhub.devhub.identity.core.auth.domain.vo.user.AuthenticatedUser;
import teamdevhub.devhub.web.shared.enums.SuccessCode;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static teamdevhub.devhub.constant.UserTestConstant.*;

class UserProfileControllerTest {

    private UserProfileController userProfileController;

    private UserProfileFacade userProfileFacade;
    private UserWithdrawFacade userWithdrawFacade;
    private BoardFacade boardFacade;
    private ProjectFacade projectFacade;

    @BeforeEach
    void init() {
        userProfileFacade = Mockito.mock(UserProfileFacade.class);
        userWithdrawFacade = Mockito.mock(UserWithdrawFacade.class);
        boardFacade = Mockito.mock(BoardFacade.class);
        projectFacade = Mockito.mock(ProjectFacade.class);

        userProfileController = new UserProfileController(userProfileFacade, userWithdrawFacade, boardFacade, projectFacade);
    }

    @Test
    @DisplayName("?좎?_?꾨줈???뺣낫_議고쉶???깃났?섎㈃_READ_SUCCESS_??肄붾뱶瑜??뺤씤?????덈떎")
    void canVerifyCodeWhenFetchingUserProfile() {
        // given
        AuthenticatedUser authenticatedUser = new AuthenticatedUser(TEST_USER_GUID_1, TEST_EMAIL_1, MemberRole.USER);
        User user = User.builder()
                .username(TEST_USERNAME_1)
                .userRole(UserRole.USER)
                .build();

        when(userProfileFacade.getCurrentUserProfile(authenticatedUser.userGuid())).thenReturn(UserDetailResponseDto.fromDomain(user, true));

        // when
        ResponseEntity<DataApiResponseDto<UserDetailResponseDto>> response = userProfileController.getProfile(authenticatedUser);

        // then
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCode()).isEqualTo(SuccessCode.READ_SUCCESS.getCode());
        assertThat(response.getBody().getData().getUser().getUsername()).isEqualTo(TEST_USERNAME_1);

        verify(userProfileFacade).getCurrentUserProfile(authenticatedUser.userGuid());
    }

    @Test
    @DisplayName("?좎?_?꾨줈???뺣낫_?섏젙???깃났?섎㈃_UPDATE_SUCCESS_??肄붾뱶瑜??뺤씤?????덈떎")
    void canVerifyCodeWhenUpdatingUserProfile() {
        // given
        AuthenticatedUser authenticatedUser = new AuthenticatedUser(TEST_USER_GUID_1, TEST_EMAIL_1, MemberRole.USER);
        UpdateProfileRequestDto updateProfileRequestDto = UpdateProfileRequestDto.builder()
                .username(NEW_USERNAME)
                .introduction(NEW_INTRO)
                .positionList(NEW_POSITION_LIST)
                .skillList(NEW_SKILL_LIST)
                .build();

        doNothing().when(userProfileFacade).updateProfile(any());

        // when
        ResponseEntity<DataApiResponseDto<Void>> response = userProfileController.updateProfile(updateProfileRequestDto, authenticatedUser);

        // then
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCode()).isEqualTo(SuccessCode.UPDATE_SUCCESS.getCode());
    }

    @Test
    @DisplayName("?뚯썝?덊눜???깃났?섎㈃_USER_DELETE_SUCCESS_??肄붾뱶瑜??뺤씤?????덈떎")
    void canVerifyCodeWhenDeletingUserAccount() {
        // given
        AuthenticatedUser authenticatedUser = new AuthenticatedUser(TEST_USER_GUID_1, TEST_EMAIL_1, MemberRole.USER);
        doNothing().when(userWithdrawFacade).withdraw(authenticatedUser.userGuid());

        // when
        ResponseEntity<DataApiResponseDto<Void>> response = userProfileController.withdraw(authenticatedUser);

        // then
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCode()).isEqualTo(SuccessCode.USER_DELETE_SUCCESS.getCode());

        verify(userWithdrawFacade).withdraw(authenticatedUser.userGuid());
    }
}
