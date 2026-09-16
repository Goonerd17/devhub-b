package teamdevhub.devhub.web.api.auth.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import teamdevhub.devhub.web.api.auth.controller.VerificationController;
import teamdevhub.devhub.web.api.auth.model.request.ConfirmVerificationRequestDto;
import teamdevhub.devhub.web.api.auth.model.request.IssueVerificationRequestDto;
import teamdevhub.devhub.identity.core.auth.port.in.facade.VerificationFacade;
import teamdevhub.devhub.web.shared.enums.SuccessCode;
import teamdevhub.devhub.web.api.web.model.response.DataApiResponseDto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static teamdevhub.devhub.constant.UserTestConstant.TEST_EMAIL_1;
import static teamdevhub.devhub.constant.UserTestConstant.TEST_EMAIL_CODE;

public class VerificationControllerTest {

    private VerificationController verificationController;

    private VerificationFacade verificationFacade;

    @BeforeEach
    void init() {
        verificationFacade = Mockito.mock(VerificationFacade.class);

        verificationController = new VerificationController(verificationFacade);
    }

    @Test
    @DisplayName("이메일_인증_메일_전송에_성공하면_VERIFICATION_SENT_코드를_확인할_수_있다")
    void canVerifyCodeWhenSendingEmailVerification() {
        // given
        IssueVerificationRequestDto issueVerificationRequestDto = new IssueVerificationRequestDto("email", TEST_EMAIL_1);
        doNothing().when(verificationFacade).issueVerification(any());

        // when
        ResponseEntity<DataApiResponseDto<Void>> response = verificationController.sendEmailVerification(issueVerificationRequestDto);

        // then
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCode()).isEqualTo(SuccessCode.VERIFICATION_SENT.getCode());

        verify(verificationFacade).issueVerification(any());
    }

    @Test
    @DisplayName("이메일_인증_확인에_성공하면_VERIFICATION_SUCCESS_코드를_확인할_수_있다")
    void canVerifyCodeWhenConfirmingEmailVerification() {
        // given
        ConfirmVerificationRequestDto confirmVerificationRequestDto = new ConfirmVerificationRequestDto(
                "email",
                TEST_EMAIL_1,
                TEST_EMAIL_CODE
        );
        doNothing().when(verificationFacade).confirmVerification(any());

        // when
        ResponseEntity<DataApiResponseDto<Void>> response = verificationController.confirmEmailVerification(confirmVerificationRequestDto);

        // then
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCode()).isEqualTo(SuccessCode.VERIFICATION_SUCCESS.getCode());

        verify(verificationFacade).confirmVerification(any());
    }
}
