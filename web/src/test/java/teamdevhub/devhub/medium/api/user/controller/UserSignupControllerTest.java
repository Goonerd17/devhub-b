package teamdevhub.devhub.web.api.user.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import teamdevhub.devhub.web.api.auth.model.response.TokenResponseDto;
import teamdevhub.devhub.web.api.terms.model.AgreeTermsRequestDto;
import teamdevhub.devhub.web.api.user.controller.UserSignupController;
import teamdevhub.devhub.web.api.user.model.SignupRequestDto;
import teamdevhub.devhub.web.api.web.model.response.DataApiResponseDto;
import teamdevhub.devhub.identity.core.auth.application.service.AuthResult;
import teamdevhub.devhub.web.api.auth.controller.CookieFactory;
import teamdevhub.devhub.web.core.user.port.in.facade.UserSignupFacade;
import teamdevhub.devhub.web.shared.enums.SuccessCode;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static teamdevhub.devhub.constant.UserTestConstant.*;

public class UserSignupControllerTest {

    private UserSignupController userSignupController;

    private UserSignupFacade userSignupFacade;

    @BeforeEach
    void init() {
        userSignupFacade = Mockito.mock(UserSignupFacade.class);
        userSignupController = new UserSignupController(userSignupFacade, new CookieFactory());
    }

    @Test
    @DisplayName("회원가입에_성공하면_SIGNUP_SUCCESS_의_코드를_확인할_수_있다")
    void canVerifyCodeWhenSignupSucceed() {
        // given
        SignupRequestDto signupRequestDto = SignupRequestDto.builder()
                .email(TEST_EMAIL_1)
                .password(TEST_PASSWORD_1)
                .username(TEST_USERNAME_1)
                .introduction(TEST_INTRO_1)
                .positionList(TEST_POSITION_LIST)
                .skillList(TEST_SKILL_LIST)
                .termsAgreementList(List.of(
                        AgreeTermsRequestDto.builder()
                                .termsGuid("TERMS1")
                                .agreed(true)
                                .build()
                ))
                .build();

        AuthResult mockResult = new AuthResult("access-token", "refresh-token");

        when(userSignupFacade.signup(any()))
                .thenReturn(mockResult);

        // when
        ResponseEntity<DataApiResponseDto<TokenResponseDto>> response = userSignupController.signup(signupRequestDto);

        // then
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCode()).isEqualTo(SuccessCode.LOGIN_SUCCESS.getCode());

        verify(userSignupFacade).signup(any());
    }
}
